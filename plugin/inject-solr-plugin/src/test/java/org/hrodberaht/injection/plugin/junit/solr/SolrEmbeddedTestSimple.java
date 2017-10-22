package org.hrodberaht.injection.plugin.junit.solr;

import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrInputDocument;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hrodberaht.injection.plugin.junit.solr.SolrAssertions.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@ContainerContext(ContainerConfigExample2.class)
@RunWith(JUnitRunner.class)
public class SolrEmbeddedTestSimple {

    @Inject
    private SolrJPlugin solrJPlugin;

    private SolrAssertions assertions;

    @PostConstruct
    public void init() {
        assertions = solrJPlugin.getAssertions();
    }


    @Test
    public void testSolrIndex() throws Exception {

        for (int i = 1; i <= 10; i++) {
            assertions.assertAddDocument(OK, makeTestDocument(i));
        }
        assertions.assertCommit(OK);
        assertions.assertExistsAndReturn("1");
        assertions.assertCount(OK, 10);

    }

    @Test
    public void testSolrIndexCloneForCleanVerification() throws Exception {

        for (int i = 1; i <= 10; i++) {
            assertions.assertAddDocument(OK, makeTestDocument(i));
        }
        assertions.assertCommit(OK);
        assertions.assertExistsAndReturn("1");
        assertions.assertCount(OK, 10);

    }


    @Test
    public void testSchemaUpdate() throws Exception {


        String fieldName = "M&A";

        List<Map<String, Object>> initialFields = new SchemaRequest.Fields().process(solrJPlugin.getClient()).getFields();

        Map<String, Object> fieldAttributes = new LinkedHashMap<>();
        fieldAttributes.put("name", fieldName);
        fieldAttributes.put("type", "string");
        fieldAttributes.put("stored", false);
        fieldAttributes.put("indexed", true);
        fieldAttributes.put("multiValued", true);
        SchemaRequest.AddField addFieldUpdateSchemaRequest = new SchemaRequest.AddField(fieldAttributes);
        SchemaResponse.UpdateResponse addFieldResponse = addFieldUpdateSchemaRequest.process(solrJPlugin.getClient());

        assertions.assertResponse(addFieldResponse);

        List<Map<String, Object>> newFields = new SchemaRequest.Fields().process(solrJPlugin.getClient()).getFields();


        assertTrue(initialFields.size() + 1 == newFields.size());

        Map<String, Object> foundFieldAttributes = new SchemaRequest.Field(fieldName).process(solrJPlugin.getClient()).getField();

        assertEquals(fieldAttributes.get("name"), foundFieldAttributes.get("name"));
        assertEquals(fieldAttributes.get("type"), foundFieldAttributes.get("type"));
        assertEquals(fieldAttributes.get("stored"), foundFieldAttributes.get("stored"));
        assertEquals(fieldAttributes.get("indexed"), foundFieldAttributes.get("indexed"));
        assertEquals(fieldAttributes.get("multiValued"), foundFieldAttributes.get("multiValued"));

    }

    private SolrInputDocument makeTestDocument(int i) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", String.valueOf(i));
        document.addField("text", "<doc><title id='" + i + "'>" + (101 - i) + "</title><test>cat</test></doc>");
        return document;
    }


}


