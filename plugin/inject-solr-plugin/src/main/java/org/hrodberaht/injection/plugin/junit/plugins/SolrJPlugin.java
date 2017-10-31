/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.plugins;

import org.apache.solr.client.solrj.SolrClient;
import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProvider;
import org.hrodberaht.injection.plugin.junit.api.resource.ResourceProviderSupport;
import org.hrodberaht.injection.plugin.junit.solr.SolrAssertions;
import org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner;

import javax.inject.Provider;
import java.util.HashSet;
import java.util.Set;

public class SolrJPlugin implements Plugin, ResourceProviderSupport {

    public static final String DEFAULT_HOME = "target/solr";

    private SolrTestRunner solrTestRunner;
    private String solrHome;
    private String coreName;
    private ResourceLifeCycle lifeCycle = ResourceLifeCycle.TEST_CONFIG;
    private PluginLifeCycledResource<SolrTestRunner> pluginLifeCycledResource = new PluginLifeCycledResource<>(SolrTestRunner.class);
    private Set<ResourceProvider> resourceProviders = new HashSet<>();



    public SolrJPlugin lifeCycle(ResourceLifeCycle resourceLifeCycle) {
        this.lifeCycle = resourceLifeCycle;
        return this;
    }

    public SolrJPlugin coreName(String coreName) {
        this.coreName = coreName;
        return this;
    }

    public SolrJPlugin solrHome(String solrHome) {
        this.solrHome = solrHome;
        return this;
    }

    public SolrJPlugin namedResource(String name, Provider instance){
        resourceProviders.add(new ResourceProvider(name, null, instance));
        return this;
    }

    public SolrJPlugin typedResource(Class type, Provider instance){
        resourceProviders.add(new ResourceProvider(null, type, instance));
        return this;
    }

    public SolrJPlugin namedTypedResource(String name, Class type, Provider instance){
        resourceProviders.add(new ResourceProvider(null, type, instance));
        return this;
    }

    public ResourceLifeCycle getResourceLifeCycle() {
        return lifeCycle;
    }

    public SolrAssertions getAssertions() {
        return solrTestRunner.solrAssertions();
    }

    public SolrClient getClient() {
        if(solrTestRunner == null){
            throw new IllegalStateException("Client can not be fetched before container creation");
        }
        return solrTestRunner.getClient();
    }


    @RunnerPluginBeforeContainerCreation
    protected void beforeContainerCreation(PluginContext pluginContext) {
        solrTestRunner = pluginLifeCycledResource.create(lifeCycle, pluginContext, this::createSolrContainer);
        if (lifeCycle == ResourceLifeCycle.TEST_SUITE || lifeCycle == ResourceLifeCycle.TEST_CONFIG) {
            prepareSolr(pluginContext);
        }
    }

    @RunnerPluginBeforeClassTest
    protected void beforeTestClass(PluginContext pluginContext) {
        if (lifeCycle == ResourceLifeCycle.TEST_CLASS) {
            prepareSolr(pluginContext);
        }
    }

    @RunnerPluginAfterClassTest
    protected void afterTestClass(PluginContext pluginContext) {
        if (lifeCycle == ResourceLifeCycle.TEST_CLASS) {
            shutdownSolr();
        }
    }

    @RunnerPluginBeforeTest
    protected void beforeTest(PluginContext pluginContext) {
        if (lifeCycle == ResourceLifeCycle.TEST) {
            prepareSolr(pluginContext);
        } else {
            solrTestRunner.cleanSolrInstance();
        }
    }

    @RunnerPluginAfterTest
    protected void afterTest(PluginContext pluginContext) {
        if (lifeCycle == ResourceLifeCycle.TEST) {
            shutdownSolr();
        }
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }

    private void shutdownSolr() {
        solrTestRunner.shutdownServer();
    }


    private void prepareSolr(PluginContext pluginContext) {
        solrTestRunner.setup(getSolrHome(pluginContext), coreName);
    }

    private String getSolrHome(PluginContext pluginContext) {
        return solrHome == null ?
                getTestDirectoryForSolr(pluginContext, DEFAULT_HOME) :
                getTestDirectoryForSolr(pluginContext, solrHome);
    }

    private String getTestDirectoryForSolr(PluginContext pluginContext, String home) {
        return pluginLifeCycledResource.testDirectory(home, pluginContext, lifeCycle);
    }


    private SolrTestRunner createSolrContainer() {
        return new SolrTestRunner();
    }


    @Override
    public Set<ResourceProvider> resources() {
        return resourceProviders;
    }
}
