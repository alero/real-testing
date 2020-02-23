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
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JerseyClientRestassuredRequest {


    private MultivaluedMap<String, Object> headers = new MultivaluedHashMap<>();
    private Map<String, String> queryParams = new HashMap<>();
    private Map<String, String> formParams = new HashMap<>();
    private String jsonStringBody;
    private Object entityObject;
    private FormDataMultiPart multipart;

    private JerseyClient client;
    private String uri;

    private JerseyClientRestassured.JerseyClientRestassuredResult result;
    private JerseyClientRestassured.JerseyClientRestassuredObjectResult objectResult;
    private Mode mode = Mode.PREPARE;
    private Integer expectedStatusCode = null;

    JerseyClientRestassuredRequest(JerseyClient client, String uri) {
        this.client = client;
        this.uri = uri;
    }

    JerseyClientRestassured getJerseyClientRestassured(String pathValue, JerseyClientRestassured.HttpMethod httpMethod) {
        return new JerseyClientRestassured(client, uri, pathValue, httpMethod);
    }

    public JerseyClientRestassuredResponse patch(String path, Object... values) {
        return patch(path, MediaType.APPLICATION_JSON_TYPE, values);
    }

    public JerseyClientRestassuredResponse patch(String path, MediaType mediaType, Object... values) {
        String pathValue = JerseyClientRestassured.replaceVariables(path, values);
        callHttpMethod(pathValue, mediaType, JerseyClientRestassured.HttpMethod.PATCH);
        return new JerseyClientRestassuredResponse(this);
    }

    public JerseyClientRestassuredResponse post(String path, Object... values) {
        return post(path, MediaType.APPLICATION_JSON_TYPE, values);
    }

    public JerseyClientRestassuredResponse post(String path, MediaType mediaType, Object... values) {
        String pathValue = JerseyClientRestassured.replaceVariables(path, values);
        callHttpMethod(pathValue, mediaType, JerseyClientRestassured.HttpMethod.POST);
        return new JerseyClientRestassuredResponse(this);
    }

    public JerseyClientRestassuredResponse put(String path, Object... values) {
        return put(path, MediaType.APPLICATION_JSON_TYPE, values);
    }

    public JerseyClientRestassuredResponse put(String path, MediaType mediaType, Object... values) {
        String pathValue = JerseyClientRestassured.replaceVariables(path, values);
        callHttpMethod(pathValue, mediaType, JerseyClientRestassured.HttpMethod.PUT);
        return new JerseyClientRestassuredResponse(this);
    }

    public JerseyClientRestassuredResponse get(String path, Object... values) {
        return get(path, MediaType.APPLICATION_JSON_TYPE, values);
    }

    public JerseyClientRestassuredResponse get(String path, MediaType mediaType, Object... values) {
        String pathValue = JerseyClientRestassured.replaceVariables(path, values);
        callHttpMethod(pathValue, mediaType, JerseyClientRestassured.HttpMethod.GET);
        return new JerseyClientRestassuredResponse(this);
    }

    public JerseyClientRestassuredResponse delete(String path, Object... values) {
        return delete(path, MediaType.APPLICATION_JSON_TYPE, values);
    }


    public JerseyClientRestassuredResponse delete(String path, MediaType mediaType, Object... values) {
        String pathValue = JerseyClientRestassured.replaceVariables(path, values);
        callHttpMethod(pathValue, mediaType, JerseyClientRestassured.HttpMethod.DELETE);
        return new JerseyClientRestassuredResponse(this);
    }

    void callHttpMethod(String pathValue, MediaType mediaType, JerseyClientRestassured.HttpMethod httpMethod) {
        if (jsonStringBody != null) {
            result = getJerseyClientRestassured(pathValue, httpMethod).call(mediaType, headers, queryParams, jsonStringBody, formParams, multipart);
            if (expectedStatusCode != null) {
                result.verifyStatusCode(expectedStatusCode);
            }
        } else {
            objectResult = getJerseyClientRestassured(pathValue, httpMethod).call(mediaType, headers, queryParams, entityObject, formParams, multipart);
            if (expectedStatusCode != null) {
                objectResult.verifyStatusCode(expectedStatusCode);
            }
        }
    }

    Response response() {
        if (objectResult != null) {
            return objectResult.getResponse();
        }
        if (result != null) {
            return result.getResponse();
        }
        throw new IllegalAccessError("no result was parsed from the request");
    }

    <T> T response(Class<T> theClazz) {
        if (objectResult != null) {
            return objectResult.getEntity(theClazz);
        }
        if (result != null) {
            return result.getEntity(theClazz);
        }
        throw new IllegalAccessError("no result was parsed from the request");
    }

    public JerseyClientRestassuredRequest header(String key, String value) {
        headers.add(key, value);
        return this;
    }

    public JerseyClientRestassuredRequest queryParam(String key, String value) {
        queryParams.put(key, value);
        return this;
    }

    public JerseyClientRestassuredRequest body(String jsonBody) {
        this.jsonStringBody = jsonBody;
        return this;
    }

    public JerseyClientRestassuredRequest formParam(String key, String value) {
        formParams.put(key, value);
        return this;
    }

    public JerseyClientRestassuredRequest bodyEntity(Object entityObject) {
        if (jsonStringBody != null) {
            throw new IllegalAccessError("Can only be string or object body: jsonStringBody is " + jsonStringBody);
        }
        this.entityObject = entityObject;
        return this;
    }

    public JerseyClientRestassuredRequest multiPart(String name, File multipartFile) {
        try(FormDataMultiPart formDataMultiPart = new FormDataMultiPart()) {
            final FileDataBodyPart filePart = new FileDataBodyPart(name, multipartFile);
            multipart = (FormDataMultiPart) formDataMultiPart.bodyPart(filePart);
            return this;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



    public JerseyClientRestassuredRequest expect() {
        mode = Mode.EXPECT;
        return this;
    }

    public JerseyClientRestassuredRequest statusCode(int statusCode) {
        if (mode != Mode.EXPECT) {
            throw new IllegalStateException("Most expect before values can be set");
        }
        expectedStatusCode = statusCode;
        return this;
    }

    public JerseyClientRestassuredRequest when() {
        mode = Mode.WHEN;
        return this;
    }

    private enum Mode {PREPARE, EXPECT, WHEN}
}
