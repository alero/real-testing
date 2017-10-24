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
import org.apache.solr.client.solrj.SolrServerException;
import org.hrodberaht.injection.core.register.InjectionRegister;
import org.hrodberaht.injection.plugin.junit.api.RunnerPlugin;
import org.hrodberaht.injection.plugin.junit.solr.SolrAssertions;
import org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner;

import java.io.IOException;

public class SolrJPlugin implements RunnerPlugin {

    private SolrTestRunner solrTestRunner = new SolrTestRunner();
    private String solrHome = null;
    private String coreName = null;

    @Override
    public void beforeContainerCreation() {

    }

    @Override
    public void afterContainerCreation(InjectionRegister injectionRegister) {
        try {
            solrTestRunner.setup(solrHome == null ? SolrTestRunner.DEAFULT_HOME : solrHome, coreName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void beforeTestClass(InjectionRegister injectionRegister) {

    }

    @Override
    public void afterTestClass(InjectionRegister injectionRegister) {

    }

    @Override
    public void beforeTest(InjectionRegister injectionRegister) {
        try {
            solrTestRunner.cleanSolrInstance();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterTest(InjectionRegister injectionRegister) {

    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.TEST_CONFIG;
    }

    public SolrJPlugin coreName(String coreName) {
        this.coreName = coreName;
        return this;
    }

    public SolrJPlugin solrHome(String solrHome) {
        this.solrHome = solrHome;
        return this;
    }

    public SolrAssertions getAssertions() {
        return solrTestRunner.solrAssertions();
    }

    public SolrClient getClient() {
        return solrTestRunner.getClient();
    }
}
