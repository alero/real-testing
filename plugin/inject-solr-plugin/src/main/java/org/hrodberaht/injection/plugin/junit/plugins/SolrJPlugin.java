package org.hrodberaht.injection.plugin.junit.plugins;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.hrodberaht.injection.plugin.junit.solr.SolrAssertions;
import org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

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
