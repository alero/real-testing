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

package org.hrodberaht.injection.plugin.junit.solr;

import org.apache.solr.client.solrj.request.schema.SchemaRequest;
import org.apache.solr.client.solrj.response.schema.SchemaResponse;
import org.apache.solr.common.SolrInputDocument;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.SolrJPlugin;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.hrodberaht.injection.plugin.junit.solr.SolrAssertions.Status.OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@ContainerContext(ContainerConfigExample.class)
@RunWith(JUnit4Runner.class)
public class SolrEmbeddedTest {


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

        // assertions.assertQuery(null, "(delete('/doc/1'),commit())");
        // assertions.assertQuery(9L, "count(collection())");


        //assertions.assertQuery("OK", "('OK',delete('/doc/2'),commit())");
        //assertions.assertQuery(8L, "count(collection())");

        //assertions.assertQuery("OK", "('OK',delete('lux:/'),commit())");
        //assertions.assertQuery(0L, "count(collection())");
    }

    @Test
    public void testSolrIndexUpdate() throws Exception {

        updateSchemaField("dynamicField");

        for (int i = 1; i <= 2; i++) {
            assertions.assertAddDocument(OK, makeTestDocument(i, "dynamicField", "dynamicValue"));
        }

        assertions.assertCommit(OK);
        assertions.assertExistsAndReturn("1");
        assertions.assertCount(OK, 2);


        assertEquals(2, assertions.find("*:*").getResults().size());
        assertEquals(2, assertions.find("dynamicField:dynamicValue").getResults().size());

        updateSchemaField("sometype");

        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", "1");
        updateSolrFieldPartial(document, "sometype", "somevalue");


        assertions.assertAddDocument(OK, document);
        assertions.assertCommit(OK);


        assertEquals(2, assertions.find("*:*").getResults().size());
        assertEquals(1, assertions.find("id:1").getResults().size());
        assertEquals("1", assertions.findFields("id:1", "id").getResults().get(0).get("id"));
        assertEquals(1, assertions.findFields("id:1", "id").getResults().get(0).getFieldNames().size());

        assertEquals(1, assertions.find("sometype:somevalue").getResults().size());
        assertEquals(2, assertions.find("filePath:path").getResults().size());
        assertEquals(2, assertions.find("dynamicField:dynamicValue").getResults().size());

    }

    private SolrInputDocument updateSolrFieldPartial(SolrInputDocument document, String key, Object value) {
        Map<String, Object> fieldModifier = new HashMap<>(1);
        fieldModifier.put("add", value);
        document.addField(key, fieldModifier);
        return document;
    }


    @Test
    public void testSchemaUpdate() throws Exception {
        String fieldName = "VerificationTest";

        List<Map<String, Object>> initialFields = new SchemaRequest.Fields().process(solrJPlugin.getClient()).getFields();

        SchemaResponse.UpdateResponse addFieldResponse = updateSchemaField(fieldName);

        assertions.assertResponse(addFieldResponse);

        List<Map<String, Object>> newFields = new SchemaRequest.Fields().process(solrJPlugin.getClient()).getFields();


        assertTrue(initialFields.size() + 1 == newFields.size());


    }

    private SchemaResponse.UpdateResponse updateSchemaField(String fieldName) throws org.apache.solr.client.solrj.SolrServerException, IOException {
        Map<String, Object> fieldAttributes = new LinkedHashMap<>();
        fieldAttributes.put("name", fieldName);
        fieldAttributes.put("type", "string");
        fieldAttributes.put("stored", true);
        fieldAttributes.put("indexed", true);
        fieldAttributes.put("multiValued", true);
        SchemaRequest.AddField addFieldUpdateSchemaRequest = new SchemaRequest.AddField(fieldAttributes);
        return addFieldUpdateSchemaRequest.process(solrJPlugin.getClient());
    }

    private SolrInputDocument makeTestDocument(int i) {
        return makeTestDocument(i, null, null);
    }

    private SolrInputDocument makeTestDocument(int i, String key, Object value) {
        SolrInputDocument document = new SolrInputDocument();
        document.addField("id", String.valueOf(i));
        document.addField("filePath", "path");
        if (key != null) {
            document.addField(key, value);
        }
        document.addField("text", "<doc><title id='" + i + "'>" + (101 - i) + "</title><test>cat</test></doc>");
        return document;
    }


}


