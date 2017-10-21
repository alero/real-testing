package org.hrodberaht.injection.plugin.junit.plugins;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.hrodberaht.injection.plugin.junit.solr.SolrAssertions;
import org.hrodberaht.injection.plugin.junit.solr.SolrTestRunner;
import org.hrodberaht.injection.plugin.junit.spi.RunnerPlugin;
import org.hrodberaht.injection.register.InjectionRegister;

import java.io.IOException;

public class SolrJPlugin implements RunnerPlugin{

    private SolrTestRunner solrTestRunner = new SolrTestRunner();
    private String coreName;

    @Override
    public void beforeContainerCreation() {

    }

    @Override
    public void afterContainerCreation(InjectionRegister injectionRegister) {

    }

    @Override
    public void beforeTest(InjectionRegister injectionRegister) {

    }

    @Override
    public void beforeTestClass(InjectionRegister injectionRegister) {
        try {
            solrTestRunner.setup(SolrTestRunner.DEAFULT_HOME, coreName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterTestClass(InjectionRegister injectionRegister) {
        try {
            solrTestRunner.tearDown(coreName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void afterTest(InjectionRegister injectionRegister) {
        try {
            solrTestRunner.cleanSolrInstance();
        } catch (SolrServerException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public LifeCycle getLifeCycle() {
        return LifeCycle.SINGLETON;
    }

    public void loadCollection(String coreName) {
        this.coreName = coreName;
    }

    public SolrAssertions getAssertions() {
        return solrTestRunner.solrAssertions();
    }

    public SolrClient getClient() {
        return solrTestRunner.getClient();
    }
}
