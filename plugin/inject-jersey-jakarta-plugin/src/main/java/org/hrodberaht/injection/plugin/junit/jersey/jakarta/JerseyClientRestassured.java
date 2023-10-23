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

package org.hrodberaht.injection.plugin.junit.jersey.jakarta;

import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.JerseyInvocation;
import org.glassfish.jersey.client.JerseyWebTarget;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.Boundary;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.hamcrest.Matcher;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import jakarta.json.JsonString;
import jakarta.json.JsonValue;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import java.io.StringReader;
import java.util.Map;

import static org.junit.Assert.assertEquals;

class JerseyClientRestassured {


    private JerseyClient jClient;
    private JerseyWebTarget webTarget;
    private HttpMethod httpMethod;
    private Response response;

    JerseyClientRestassured(JerseyClient client, String uri, String path, HttpMethod httpMethod) {
        this.jClient = client;
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

    JerseyClientRestassuredResult call(MediaType mediaType, final MultivaluedMap<String, Object> headers, Map<String, String> queryParams, final String stringBody, Map<String, String> formParams, FormDataMultiPart multipart) {
        addQueryParamsToTarget(queryParams);
        if(multipart != null){
            addFormParamsToMultipart(formParams, multipart);
            if (executeCall(headers, multipart)) return new JerseyClientRestassuredResult(response);
            throw new IllegalAccessError("not prepared for  call");
        }
        Entity<Object> entity = getObjectEntity(mediaType, stringBody);
        if (executeCall(mediaType, headers, entity)) return new JerseyClientRestassuredResult(response);
        throw new IllegalAccessError("not prepared for  call");

    }

    private void addFormParamsToMultipart(Map<String, String> formParams, FormDataMultiPart multipart) {
        for (String key : formParams.keySet()) {
            String value = formParams.get(key);
            multipart.field(key, value);
        }
    }

    JerseyClientRestassuredObjectResult call(MediaType mediaType, final MultivaluedMap<String, Object> headers, Map<String, String> queryParams, Object entityObject, Map<String, String> formParams, FormDataMultiPart multipart) {
        addQueryParamsToTarget(queryParams);

        if(multipart != null){
            addFormParamsToMultipart(formParams, multipart);
            if (executeCall(headers, multipart)) return new JerseyClientRestassuredObjectResult(response);
            throw new IllegalAccessError("not prepared for  call");
        }
        Entity<Object> entity = getObjectEntity(mediaType, entityObject);
        if (executeCall(mediaType, headers, entity)) {
            return new JerseyClientRestassuredObjectResult(response);
        }
        throw new IllegalAccessError("not prepared for  call");
    }

    private Entity<Object> getObjectEntity(MediaType mediaType, Object entityObject) {
        if (entityObject instanceof Entity) {
            return (Entity<Object>) entityObject;
        }
        if(mediaType == MediaType.APPLICATION_JSON_TYPE ) {
            return Entity.json(entityObject);
        }
        return Entity.entity(entityObject, mediaType);
    }

    private void addQueryParamsToTarget(Map<String, String> queryParams) {
        for (String key : queryParams.keySet()) {
            String value = queryParams.get(key);
            this.webTarget = this.webTarget.queryParam(key, value);
        }
    }

    boolean executeCall(final MultivaluedMap<String, Object> headers, FormDataMultiPart multipart) {
        try {
            if (httpMethod == HttpMethod.POST) {
                response = webTarget.request(MediaType.MULTIPART_FORM_DATA).headers(headers).post(Entity.entity(multipart, Boundary.addBoundary(MediaType.MULTIPART_FORM_DATA_TYPE)));
                return true;
            } else if (httpMethod == HttpMethod.PUT) {
                response = webTarget.request(MediaType.MULTIPART_FORM_DATA).headers(headers).put(Entity.entity(multipart, Boundary.addBoundary(MediaType.MULTIPART_FORM_DATA_TYPE)));
                return true;
            }
        }finally {
            try {
                multipart.close();
            } catch (IOException e) {
            }
        }
        return false;
    }

    boolean executeCall(MediaType mediaType, final MultivaluedMap<String, Object> headers, Entity<Object> entity) {
        if (httpMethod == HttpMethod.PATCH) {
            response = webTarget.request(mediaType).headers(headers).method("PATCH", entity);
            return true;
        } else if (httpMethod == HttpMethod.POST) {
            response = webTarget.request(mediaType).headers(headers).post(entity);
            return true;
        } else if (httpMethod == HttpMethod.GET) {
            response = webTarget.request(mediaType).headers(headers).get();
            return true;
        } else if (httpMethod == HttpMethod.PUT) {
            response = webTarget.request(mediaType).headers(headers).put(entity);
            return true;
        } else if (httpMethod == HttpMethod.DELETE) {
            response = webTarget.request(mediaType).headers(headers).delete();
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
