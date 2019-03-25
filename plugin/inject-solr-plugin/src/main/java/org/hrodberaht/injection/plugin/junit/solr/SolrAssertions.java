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

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.request.UpdateRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.SolrResponseBase;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.hrodberaht.injection.plugin.junit.solr.SolrAssertions.Status.OK;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SolrAssertions {

    private final SolrClient solr;
    private final String username;
    private final String password;
    private final String collection;

    public enum Status {OK, FAIL}

    public SolrAssertions(SolrClient solr) {
        this.solr = solr;
        this.collection = null;
        this.username = null;
        this.password = null;
    }

    public SolrAssertions(SolrClient solr, String collection, String username, String password) {
        this.solr = solr;
        this.collection = collection;
        this.username = username;
        this.password = password;
    }

    public void assertAddDocument(Status status, SolrInputDocument documents) throws IOException, SolrServerException {
        UpdateResponse updateResponse = processUpdateRequest(collection, documents);
        if (status == OK) {
            assertResponse(updateResponse);
        } else {
            assertTrue("Status not FAILED", updateResponse.getStatus() > 0);
        }
    }

    public void cleanDataFromCollection() throws IOException, SolrServerException {
        processDelete("*:*");
        processCommit();
    }


    public void waitForAsyncCommit() {
        // TODO: Async needs a callback solution
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public SolrDocument assertExistsAndReturn(String id) throws IOException, SolrServerException {
        SolrDocument solrDocument = processById(id);
        assertNotNull(solrDocument);
        assertTrue("Document is empty", solrDocument.size() > 0);
        return solrDocument;
    }


    public void assertCommit(Status status) throws IOException, SolrServerException {
        UpdateResponse updateResponse = processCommit();
        if (status == OK) {
            assertResponse(updateResponse);
        } else {
            assertTrue("Status not FAILED", updateResponse.getStatus() > 0);
        }
    }


    public QueryResponse find(String query, String filterQuery) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setQuery(query)
                .addFilterQuery(filterQuery)
                .setStart(0)
                .setRows(10);
        return processQuery(solrQuery);
    }

    public QueryResponse find(String query) throws IOException, SolrServerException {
        return find(query, 10);
    }

    public QueryResponse find(String query, int rows) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setQuery(query)
                .setStart(0)
                .setRows(rows);
        return processQuery(solrQuery);
    }


    public QueryResponse findFields(String query, String... fields) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setFields(fields)
                .setQuery(query)
                .setStart(0)
                .setRows(10);
        return processQuery(solrQuery);
    }

    public QueryResponse findFields(String query, int rows, String... fields) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setFields(fields)
                .setQuery(query)
                .setStart(0)
                .setRows(rows);
        return processQuery(solrQuery);
    }

    public void assertResponse(SolrResponseBase response) {
        assertTrue(response.getStatus() == 0);
        if (response.getResponse().get("errors") != null) {
            try {
                String message = String.valueOf(
                        ((ArrayList)
                                ((LinkedHashMap)
                                        ((ArrayList) response.getResponse().get("errors"))
                                                .get(0))
                                        .get("errorMessages"))
                                .get(0));
                assertTrue(message, response.getResponse().get("errors") == null);
            } catch (Exception e) {
                assertTrue("Generic error, found errors but unable to find message",
                        response.getResponse().get("errors") == null);
            }
        }
    }

    public void assertCount(Status status, int count) throws IOException, SolrServerException {
        assertCount(status, count, "*:*");
    }

    public void assertCount(Status status, int count, String query) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setQuery(query)
                .setStart(0)
                .setRows(0);

        QueryResponse queryResponse = processQuery(solrQuery);
        if (status == OK) {
            assertTrue("Status not OK status " + queryResponse.getStatus() + " results: " +
                            count + " actual " + queryResponse.getResults().getNumFound(),
                    queryResponse.getStatus() == 0
                            && queryResponse.getResults().getNumFound() == count);
        } else {
            assertTrue("Status not FAILED", queryResponse.getStatus() > 0);
        }
    }

    private void processDelete(String query) throws IOException, SolrServerException {
        if (username != null) {
            UpdateRequest request = new UpdateRequest();
            request.deleteByQuery(query);
            appendBasicAuth(request);
            request.process(solr, collection);
        } else {
            solr.deleteByQuery(collection, query);
        }
    }

    private QueryResponse processQuery(SolrQuery solrQuery) throws SolrServerException, IOException {
        if (username != null) {
            QueryRequest request = new QueryRequest(solrQuery);
            appendBasicAuth(request);
            return request.process(solr, collection);
        }
        return solr.query(collection, solrQuery);
    }

    private UpdateResponse processCommit() throws SolrServerException, IOException {
        if (username != null) {
            UpdateRequest request = new UpdateRequest();
            request.setAction(UpdateRequest.ACTION.COMMIT, true, true, false);
            appendBasicAuth(request);
            return request.process(solr, collection);
        }
        return solr.commit(collection, true, true, false);
    }

    private SolrDocument processById(String id) throws SolrServerException, IOException {
        if (username != null) {
            org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                    .setQuery("id:" + id)
                    .setStart(0)
                    .setRows(1);
            QueryRequest request = new QueryRequest(solrQuery);
            appendBasicAuth(request);
            return request.process(solr, collection).getResults().get(0);
        }
        return solr.getById(collection, id);
    }

    private UpdateResponse processUpdateRequest(String collection, SolrInputDocument documents) throws IOException, SolrServerException {
        if (username != null) {
            UpdateRequest request = new UpdateRequest();
            request.add(documents);
            appendBasicAuth(request);
            return request.process(solr, collection);
        }
        return solr.add(collection, documents);
    }

    private void appendBasicAuth(SolrRequest request) {
        if (username != null) {
            request.setBasicAuthCredentials(username, password);
        }
    }

}
