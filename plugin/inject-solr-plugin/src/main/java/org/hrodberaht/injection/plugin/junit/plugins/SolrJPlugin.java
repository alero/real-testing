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
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterContainerCreation;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginAfterTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeClassTest;
import org.hrodberaht.injection.plugin.junit.api.annotation.RunnerPluginBeforeTest;
import org.hrodberaht.injection.plugin.junit.solr.SolrAssertions;
import org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner;

public class SolrJPlugin implements Plugin {

    private SolrTestRunner solrTestRunner;
    private String solrHome;
    private String coreName;
    private ResourceLifeCycle lifeCycle = ResourceLifeCycle.TEST_CONFIG;
    private PluginLifeCycledResource<SolrTestRunner> pluginLifeCycledResource = new PluginLifeCycledResource<>(SolrTestRunner.class);


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

    public ResourceLifeCycle getResourceLifeCycle() {
        return lifeCycle;
    }

    public SolrAssertions getAssertions() {
        return solrTestRunner.solrAssertions();
    }

    public SolrClient getClient() {
        return solrTestRunner.getClient();
    }

    @RunnerPluginAfterContainerCreation
    private void afterContainerCreation(PluginContext pluginContext) {
        solrTestRunner = pluginLifeCycledResource.create(lifeCycle, pluginContext, this::createSolrContainer);
        if (lifeCycle == ResourceLifeCycle.TEST_SUITE || lifeCycle == ResourceLifeCycle.TEST_CONFIG) {
            prepareSolr(pluginContext);
        }
    }

    private void prepareSolr(PluginContext pluginContext) {
        solrTestRunner.setup(getSolrHome(pluginContext), coreName);
    }

    private String getSolrHome(PluginContext pluginContext) {
        return solrHome == null ?
                getTestDirectoryForSolr(pluginContext, SolrTestRunner.DEFAULT_HOME) :
                getTestDirectoryForSolr(pluginContext, solrHome);
    }

    private String getTestDirectoryForSolr(PluginContext pluginContext, String home) {
        return pluginLifeCycledResource.testDirectory(home, pluginContext, lifeCycle);
    }


    private SolrTestRunner createSolrContainer() {
        return new SolrTestRunner();
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

    private void shutdownSolr() {
        solrTestRunner.shutdownServer();
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_SUITE;
    }




}
