package org.hrodberaht.injection.plugin.junit.solr.v5.megatest.part2;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit5Extension;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.hrodberaht.injection.plugin.junit.solr.TestSolrEmbedded;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@ContainerContext(SolrTestConfig2.class)
@ExtendWith(JUnit5Extension.class)

public class JUnitSolrEmbedded7 {

    @Inject
    public SolrJPlugin solrJPlugin;
    TestSolrEmbedded testSolrEmbedded = new TestSolrEmbedded();

    @PostConstruct
    public void init() {
        testSolrEmbedded.solrJPlugin = solrJPlugin;
        testSolrEmbedded.assertions = solrJPlugin.getAssertions();
    }

    @Test
    public void test1() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test2() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test3() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test4() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test5() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test6() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test7() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test8() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test9() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test10() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test11() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test12() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test13() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test14() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

    @Test
    public void test15() throws Exception {
        testSolrEmbedded.testSolrIndexUpdate();
    }

}
