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

package org.hrodberaht.injection.plugin.junit.plugins.megatest;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.JerseyPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.resource.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@ContainerContext(ContainerConfigMega.class)
@RunWith(JUnit4Runner.class)
public class JerseyPluginJUnit2 {

    @Inject
    JerseyPlugin jerseyPlugin;

    @Test
    public void testIsAlive() throws Exception {

        String response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/isAlive").response(String.class);

        assertEquals("OK", response);

    }

    @Test
    public void testFind1() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind2() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind3() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind4() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind5() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind6() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind7() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind8() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind9() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }

    @Test
    public void testFind10() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }
}