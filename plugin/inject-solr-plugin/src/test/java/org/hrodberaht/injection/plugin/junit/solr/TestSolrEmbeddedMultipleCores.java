package org.hrodberaht.injection.plugin.junit.solr;

import org.apache.solr.common.SolrException;
import org.apache.solr.common.SolrInputDocument;
import org.hrodberaht.injection.core.stream.InjectionRegistryBuilder;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.ContainerContextConfigBase;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static org.hrodberaht.injection.plugin.junit.solr.SolrAssertions.Status.OK;

@ContainerContext(TestSolrEmbeddedMultipleCores.Config.class)
@RunWith(JUnit4Runner.class)
public class TestSolrEmbeddedMultipleCores {

    @Inject
    private SolrJPlugin solrJPlugin;

    private SolrAssertions collection1Assertions;
    private SolrAssertions collection2Assertions;

    @PostConstruct
    public void init() {
        collection1Assertions = solrJPlugin.getAssertions("collection1");
        collection2Assertions = solrJPlugin.getAssertions("collection2");
    }

    @Test
    public void testSolrIndexForCore1() throws Exception {
        boolean includeCollection1SpecificField = true;
        addAndAssert(collection1Assertions, includeCollection1SpecificField);
    }

    @Test
    public void testSolrIndexForCore2() throws Exception {
        boolean includeCollection1SpecificField = false;
        addAndAssert(collection2Assertions, includeCollection1SpecificField);
    }

    @Test(expected = SolrException.class)
    public void shouldFailOnMissingFieldInCollection2() throws Exception {
        boolean includeCollection1SpecificField = true;
        addAndAssert(collection2Assertions, includeCollection1SpecificField);
    }

    private void addAndAssert(SolrAssertions assertions, boolean includeCollection1SpecificField) throws Exception {
        for (int i = 1; i <= 10; i++) {
            assertions.assertAddDocument(OK, makeTestDocument(i, includeCollection1SpecificField));
        }
        assertions.assertCommit(OK);
        assertions.assertExistsAndReturn("1");
        assertions.assertCount(OK, 10);

        assertions.cleanDataFromCollection();

        assertions.assertCount(OK, 0);
    }

    private SolrInputDocument makeTestDocument(int i, boolean includeFilePath) {
        return makeTestDocument(i, null, null, includeFilePath);
    }

    private SolrInputDocument makeTestDocument(int i, String key, Object value, boolean includeFilePath) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", String.valueOf(i));
        if (includeFilePath) {
            document.addField("filePath", "path");
        }
        if (key != null) {
            document.addField(key, value);
        }
        document.addField("text", "<doc><title id='" + i + "'>" + (101 - i) + "</title><test>cat</test></doc>");
        return document;
    }

    public static class Config extends ContainerContextConfigBase {

        @Override
        public void register(InjectionRegistryBuilder registryBuilder) {
            activatePlugin(SolrJPlugin.class)
                    .solrRunnerHome("target/solr3")
                    .coreName("collection1", "collection2");
        }
    }
}
