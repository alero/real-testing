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

package org.hrodberaht.injection.plugin.junit.plugins.jakarta;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.hrodberaht.injection.plugin.junit.plugins.JerseyJakartaPlugin;
import org.hrodberaht.injection.plugin.junit.plugins.jakarta.config.ContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.plugins.jakarta.resource.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@ContainerContext(ContainerConfigExample.class)
@RunWith(JUnit4Runner.class)
public class JerseyPluginTest2 {

    @Inject
    JerseyJakartaPlugin jerseyPlugin;

    @Test
    public void testIsAlive() throws Exception {

        String response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/isAlive").response(String.class);

        assertEquals("OK", response);

    }

    @Test
    public void testFind() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }
}