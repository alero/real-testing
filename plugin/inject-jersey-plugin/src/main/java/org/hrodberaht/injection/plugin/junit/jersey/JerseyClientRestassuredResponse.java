package org.hrodberaht.injection.plugin.junit.jersey;

import javax.ws.rs.core.Response;

public class JerseyClientRestassuredResponse {

    JerseyClientRestassuredRequest jerseyClientRestassuredRequest;

    public JerseyClientRestassuredResponse(JerseyClientRestassuredRequest jerseyClientRestassuredRequest) {
        this.jerseyClientRestassuredRequest = jerseyClientRestassuredRequest;
    }

    public Response response() {
        return jerseyClientRestassuredRequest.response();
    }

    public <T> T response(Class<T> type) {
        return jerseyClientRestassuredRequest.response(type);
    }
}
