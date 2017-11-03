package org.hrodberaht.injection.plugin.junit.plugins;

import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.plugin.junit.plugins.common.PluginLifeCycledResource;

public class ActiveMQPlugin implements Plugin {

    private PluginLifeCycledResource<EmbeddedActiveMQBroker> pluginLifeCycledResource = new PluginLifeCycledResource<>(EmbeddedActiveMQBroker.class);

    private EmbeddedActiveMQBroker embedded;

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_CONFIG;
    }


    @RunnerPluginBeforeContainerCreation
    public void beforeContainer(PluginContext pluginContext){
        embedded = pluginLifeCycledResource.create(getLifeCycle(), pluginContext, () -> {
            EmbeddedActiveMQBroker embeddedActiveMQBroker = new EmbeddedActiveMQBroker();
            if(getLifeCycle() == LifeCycle.TEST_SUITE || getLifeCycle() == LifeCycle.TEST_CONFIG) {
                embeddedActiveMQBroker.start();
            }
            return embeddedActiveMQBroker;
        });

    }

    @RunnerPluginBeforeClassTest
    public void beforeTestClass(){
        if(getLifeCycle() == LifeCycle.TEST_CLASS){
            embedded.start();
        }
    }

    @RunnerPluginAfterClassTest
    public void afterTestClass(){
        if(getLifeCycle() == LifeCycle.TEST_CLASS){
            embedded.stop();
        }
    }

    @RunnerPluginBeforeTest
    public void beforeTest(){
        if(getLifeCycle() == LifeCycle.TEST){
            embedded.start();
        }
    }

    @RunnerPluginAfterTest
    public void afterTest(){
        if(getLifeCycle() == LifeCycle.TEST){
            embedded.stop();
        }
    }

    public EmbeddedActiveMQBroker getEmbedded() {
        return embedded;
    }
}
