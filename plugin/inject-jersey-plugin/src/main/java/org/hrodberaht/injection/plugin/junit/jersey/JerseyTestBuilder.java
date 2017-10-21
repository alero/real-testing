package org.hrodberaht.injection.plugin.junit.jersey;

import org.glassfish.jersey.test.JerseyTest;

public class JerseyTestBuilder {

    private final String url;
    private final JerseyTest jerseyTest;

    public JerseyTestBuilder(String url, JerseyTest jerseyTest) {
        this.url = url;
        this.jerseyTest = jerseyTest;
    }

    public String getUrl() {
        return url;
    }

    public JerseyClientRestassuredResponse get(String path) {
        return new JerseyClientRestassuredRequest(jerseyTest.client(), url).get(path);
    }

    public JerseyClientRestassuredResponse get(String path, Object... values) {
        return new JerseyClientRestassuredRequest(jerseyTest.client(), url).get(path, values);
    }

    public JerseyClientRestassuredRequest given() {
        return new JerseyClientRestassuredRequest(jerseyTest.client(), url);
    }

    private JerseyClientRestassured createClient(String path, Object... values) {
        String pathValue = JerseyClientRestassured.replaceVariables(path, values);
        return new JerseyClientRestassured(jerseyTest.client(), url, pathValue, JerseyClientRestassured.HttpMethod.GET);
    }

}
