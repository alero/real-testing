package org.hrodberaht.injection.plugin.junit.plugins;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProvider;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProviderSupport;
import org.hrodberaht.injection.plugin.junit.plugins.common.PluginLifeCycledResource;

import javax.jms.ConnectionFactory;
import java.util.HashSet;
import java.util.Set;

public class ActiveMQPlugin implements Plugin, ResourceProviderSupport {

    private PluginLifeCycledResource<EmbeddedActiveMQBroker> pluginLifeCycledResource = new PluginLifeCycledResource<>(EmbeddedActiveMQBroker.class);

    private EmbeddedActiveMQBroker embedded;
    private String name = null;
    private LifeCycle lifeCycle = LifeCycle.TEST_SUITE;

    @Override
    public LifeCycle getLifeCycle() {
        return lifeCycle;
    }

    public ActiveMQPlugin lifeCycle(final LifeCycle lifeCycle) {
        this.lifeCycle = lifeCycle;
        if (LifeCycle.TEST_CONFIG == lifeCycle) {
            throw new IllegalArgumentException("lifeCycle can not be set to TEST_CONFIG as EmbeddedActiveMQBroker does not support multiple instances in one test suite");
        }
        return this;
    }

    /**
     * To be able to give the resource a name
     *
     * @param name of the Resource that is exposed for type ConnectionFactory
     * @return builder
     */
    public ActiveMQPlugin name(final String name) {
        this.name = name;
        return this;
    }

    @RunnerPluginBeforeContainerCreation
    public void beforeContainer(final PluginContext pluginContext) {
        embedded = pluginLifeCycledResource.create(getLifeCycle(), pluginContext, () -> {
            EmbeddedActiveMQBroker embeddedActiveMQBroker = new EmbeddedActiveMQBroker();
            if (getLifeCycle() == LifeCycle.TEST_SUITE) {
                embeddedActiveMQBroker.start();
            }
            return embeddedActiveMQBroker;
        });
    }

    @RunnerPluginBeforeClassTest
    public void beforeTestClass() {
        if (getLifeCycle() == LifeCycle.TEST_CLASS) {
            embedded.start();
        }
    }

    @RunnerPluginAfterClassTest
    public void afterTestClass() {
        if (getLifeCycle() == LifeCycle.TEST_CLASS) {
            embedded.stop();
        }
    }

    @RunnerPluginBeforeTest
    public void beforeTest() {
        if (getLifeCycle() == LifeCycle.TEST) {
            embedded.start();
        }
    }

    @RunnerPluginAfterTest
    public void afterTest() {
        if (getLifeCycle() == LifeCycle.TEST) {
            embedded.stop();
        }
    }

    public EmbeddedActiveMQBroker getEmbedded() {
        return embedded;
    }

    @Override
    public Set<ResourceProvider> resources() {
        if (getLifeCycle() == LifeCycle.TEST || getLifeCycle() == LifeCycle.TEST_CLASS) {
            throw new IllegalArgumentException("Lifecycle can not be TEST or TEST_CLASS when connecting resources, this is because the MQ has to be started before the Resource is used");
        }
        Set<ResourceProvider> resourceProviders = new HashSet<>();
        resourceProviders.add(new ResourceProvider(name, ConnectionFactory.class, () -> embedded.createConnectionFactory()));
        return resourceProviders;
    }
}
