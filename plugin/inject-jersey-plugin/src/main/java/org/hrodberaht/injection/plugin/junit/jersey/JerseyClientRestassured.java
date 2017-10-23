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

package org.hrodberaht.injection.plugin.junit.jersey;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.logging.LoggingFeature;
import org.hamcrest.Matcher;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.StringReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class JerseyClientRestassured {


    private JerseyClient jClient;
    private JerseyWebTarget webTarget;
    private HttpMethod httpMethod;
    private Response response;

    JerseyClientRestassured(Client client, String uri, String path, HttpMethod httpMethod) {
        this.jClient = (JerseyClient) client;
        this.jClient = this.jClient.property(LoggingFeature.LOGGING_FEATURE_VERBOSITY_CLIENT, LoggingFeature.Verbosity.PAYLOAD_ANY);
        this.webTarget = jClient.target(uri).path(path);
        this.httpMethod = httpMethod;
    }

    static String replaceVariables(String path, Object[] values) {
        String localPath = path;
        if (values == null) {
            return path;
        }
        for (Object value : values) {
            if (value == null) {
                throw new IllegalAccessError("Value can not be null");
            }
            int start = localPath.indexOf("{");
            int end = localPath.indexOf("}");
            localPath = localPath.substring(0, start) + value.toString() + localPath.substring(end + 1);
        }
        return localPath;
    }

    JerseyClientRestassuredResult get() {
        if (httpMethod != HttpMethod.GET) {
            throw new IllegalAccessError("only prepared for get");
        }
        JerseyInvocation.Builder builder = webTarget.request(MediaType.APPLICATION_JSON_TYPE);
        return new JerseyClientRestassuredResult(builder.get());
    }

    JerseyClientRestassuredResult call(final MultivaluedMap<String, Object> headers, Map<String, String> queryParams, final String jsonBody) {
        addQueryParamsToTarget(queryParams);
        Entity<Object> entity = getObjectEntity(jsonBody);
        if (executeCall(headers, entity)) return new JerseyClientRestassuredResult(response);
        throw new IllegalAccessError("not prepared for  call");

    }

    JerseyClientRestassuredObjectResult call(final MultivaluedMap<String, Object> headers, Map<String, String> queryParams, final Object entityObject) {
        addQueryParamsToTarget(queryParams);
        Entity<Object> entity = getObjectEntity(entityObject);
        if (executeCall(headers, entity)) {
            return new JerseyClientRestassuredObjectResult(response);
        }
        throw new IllegalAccessError("not prepared for  call");
    }

    private Entity<Object> getObjectEntity(Object entityObject) {
        if (entityObject instanceof Entity) {
            return (Entity<Object>) entityObject;
        }
        return Entity.json(entityObject);
    }

    private void addQueryParamsToTarget(Map<String, String> queryParams) {
        for (String key : queryParams.keySet()) {
            String value = queryParams.get(key);
            this.webTarget = this.webTarget.queryParam(key, value);
        }
    }

    boolean executeCall(final MultivaluedMap<String, Object> headers, Entity<Object> entity) {
        if (httpMethod == HttpMethod.PATCH) {
            response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).headers(headers).method("PATCH", entity);
            return true;
        } else if (httpMethod == HttpMethod.POST) {
            response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).headers(headers).post(entity);
            return true;
        } else if (httpMethod == HttpMethod.GET) {
            response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).headers(headers).get();
            return true;
        } else if (httpMethod == HttpMethod.PUT) {
            response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).headers(headers).put(entity);
            return true;
        } else if (httpMethod == HttpMethod.DELETE) {
            response = webTarget.request(MediaType.APPLICATION_JSON_TYPE).headers(headers).delete();
            return true;
        }
        return false;
    }

    enum HttpMethod {GET, POST, PUT, PATCH, DELETE}

    class JerseyClientRestassuredObjectResult {
        private final Response response;

        JerseyClientRestassuredObjectResult(Response response) {
            this.response = response;
        }

        <T> T getEntity(Class<T> theClazz) {
            return response.readEntity(theClazz);
        }

        Response getResponse() {
            return response;
        }

        void verifyStatusCode(final int code) {
            assertEquals(response.getStatusInfo().getReasonPhrase(), code, response.getStatus());
        }
    }

    class JerseyClientRestassuredResult {
        private final Response response;
        private JsonObject jsonObject;

        JerseyClientRestassuredResult(final Response response) {
            this.response = response;
        }

        <T> T getEntity(Class<T> theClazz) {
            return response.readEntity(theClazz);
        }

        Response getResponse() {
            return response;
        }

        private JsonObject getJSONObject(final String stringBody) {
            final StringReader stringReader = new StringReader(stringBody);
            try (final JsonReader jsonReader = Json.createReader(stringReader)) {
                return jsonReader.readObject();
            }
        }

        JerseyClientRestassuredResult readJsonObject() {
            jsonObject = getJSONObject(response.readEntity(String.class));
            return this;
        }

        JerseyClientRestassuredResult verifyStatusCode(int code) {
            assertEquals(response.getStatusInfo().getReasonPhrase(), code, response.getStatus());
            return this;
        }

        JerseyClientRestassuredResult body(final String var, final Matcher<String> stringMatcher) {
            String value = findJsonValue(String.class, var, jsonObject);
            if (!stringMatcher.matches(value)) {
                throw new AssertionError("stringMatcher does not match " + stringMatcher.toString());
            }
            return this;
        }

        private <T> T findJsonValue(final Class<T> aClass, final String var, final JsonObject jsonObject) {
            String[] objectMap = var.split("\\.");
            JsonObject jsonObjectInner = jsonObject;
            for (String key : objectMap) {
                JsonValue value = jsonObjectInner.get(key);
                if (value instanceof JsonObject) {
                    jsonObjectInner = (JsonObject) value;
                } else if (value.getValueType() == JsonValue.ValueType.STRING) {
                    return (T) ((JsonString) value).getString();
                }
            }
            return null;
        }
    }
}
