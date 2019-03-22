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
import org.hrodberaht.injection.plugin.junit.plugins.common.PluginLifeCycledResource;
import org.hrodberaht.injection.plugin.junit.solr.SolrAssertions;
import org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrJPlugin implements Plugin {

    public static final String DEFAULT_HOME = "classpath:solr";
    public static final String DEFAULT_RUNNER_HOME = "target/solr";
    private static final Logger LOG = LoggerFactory.getLogger(SolrJPlugin.class);
    private SolrTestRunner solrTestRunner;
    private String solrHome = DEFAULT_HOME;
    private String sealRunnerHome = DEFAULT_RUNNER_HOME;
    private String defaultCoreName;
    private String[] extraCores;
    private LifeCycle lifeCycle = LifeCycle.TEST_CONFIG;
    private PluginLifeCycledResource<SolrTestRunner> pluginLifeCycledResource = new PluginLifeCycledResource<>(SolrTestRunner.class);


    public SolrJPlugin lifeCycle(LifeCycle resourceLifeCycle) {
        this.lifeCycle = resourceLifeCycle;
        return this;
    }

    public SolrJPlugin coreName(String defaultCoreName, String ... extraCoreNames) {
        this.defaultCoreName = defaultCoreName;
        this.extraCores = extraCoreNames;
        return this;
    }

    public SolrJPlugin solrHome(String solrHome) {
        this.solrHome = solrHome;
        return this;
    }

    public SolrJPlugin solrRunnerHome(String solrRunnerHome) {
        this.sealRunnerHome = solrRunnerHome;
        return this;
    }

    public SolrAssertions getAssertions() {
        return solrTestRunner.solrAssertions();
    }

    public SolrAssertions getAssertions(String coreName) {
        return solrTestRunner.solrAssertions(coreName);
    }

    public SolrClient getClient() {
        if (solrTestRunner == null) {
            throw new IllegalStateException("Client can not be fetched before container creation");
        }
        return solrTestRunner.getClient();
    }


    @RunnerPluginBeforeContainerCreation
    protected void beforeContainerCreation(PluginContext pluginContext) {
        solrTestRunner = pluginLifeCycledResource.create(lifeCycle, pluginContext, this::createSolrContainer);
        if (lifeCycle == LifeCycle.TEST_SUITE || lifeCycle == LifeCycle.TEST_CONFIG) {
            prepareSolr(pluginContext);
        }
    }

    @RunnerPluginBeforeClassTest
    protected void beforeTestClass(PluginContext pluginContext) {
        if (lifeCycle == LifeCycle.TEST_CLASS) {
            prepareSolr(pluginContext);
        }
    }

    @RunnerPluginAfterClassTest
    protected void afterTestClass(PluginContext pluginContext) {
        if (lifeCycle == LifeCycle.TEST_CLASS) {
            shutdownSolr();
        }
    }

    @RunnerPluginBeforeTest
    protected void beforeTest(PluginContext pluginContext) {
        if (lifeCycle == LifeCycle.TEST) {
            prepareSolr(pluginContext);
        } else {
            solrTestRunner.cleanSolrInstance();
        }
    }

    @RunnerPluginAfterTest
    protected void afterTest(PluginContext pluginContext) {
        if (lifeCycle == LifeCycle.TEST) {
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
        solrTestRunner.setup(solrHome, getSolrHome(pluginContext), defaultCoreName, extraCores);
    }

    private String getSolrHome(PluginContext pluginContext) {
        return sealRunnerHome == null ?
                getTestDirectoryForSolr(pluginContext, DEFAULT_HOME) :
                getTestDirectoryForSolr(pluginContext, sealRunnerHome);
    }

    private String getTestDirectoryForSolr(PluginContext pluginContext, String home) {
        return pluginLifeCycledResource.testDirectory(home, pluginContext, lifeCycle);
    }


    private SolrTestRunner createSolrContainer() {
        LOG.info("createSolrContainer");
        return new SolrTestRunner();
    }


}
