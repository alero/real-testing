package org.hrodberaht.injection.plugin.junit.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
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

    public void cleanDataFromCollection() throws IOException, SolrServerException {
        solr.deleteByQuery("*:*");
        solr.commit(true, true, false);
    }


    public enum Status {OK, FAIL}

    public SolrAssertions(SolrClient solr) {
        this.solr = solr;
    }

    public void assertAddDocument(Status status, SolrInputDocument documents) throws IOException, SolrServerException {
        UpdateResponse updateResponse = solr.add(documents);
        if (status == OK) {
            assertResponse(updateResponse);
        } else {
            assertTrue("Status not FAILED", updateResponse.getStatus() > 0);
        }
    }

    public SolrDocument assertExistsAndReturn(String id) throws IOException, SolrServerException {
        SolrDocument solrDocument = solr.getById(id);
        assertNotNull(solrDocument);
        assertTrue("Document is empty", solrDocument.size() > 0);
        return solrDocument;
    }

    public void assertCommit(Status status) throws IOException, SolrServerException {
        UpdateResponse updateResponse = solr.commit(true, true, false);
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
        return solr.query(solrQuery);
    }

    public QueryResponse find(String query) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setQuery(query)
                .setStart(0)
                .setRows(10);
        return solr.query(solrQuery);
    }

    public QueryResponse findFields(String query, String... fields) throws IOException, SolrServerException {
        org.apache.solr.client.solrj.SolrQuery solrQuery = new org.apache.solr.client.solrj.SolrQuery()
                .setFields(fields)
                .setQuery(query)
                .setStart(0)
                .setRows(10);
        return solr.query(solrQuery);
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
        QueryResponse queryResponse = solr.query(solrQuery);
        if (status == OK) {
            assertTrue("Status not OK status " + queryResponse.getStatus() + " results: " +
                            count + " actual " + queryResponse.getResults().getNumFound(),
                    queryResponse.getStatus() == 0
                            && queryResponse.getResults().getNumFound() == count);
        } else {
            assertTrue("Status not FAILED", queryResponse.getStatus() > 0);
        }
    }


}
