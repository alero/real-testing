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

package org.hrodberaht.injection.plugin.junit.plugins.jakarta.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Path("api")
public class ExampleResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleResource.class);


    @GET
    @Path("isAlive")
    @Produces(MediaType.APPLICATION_JSON)
    public String isAlive() {
        return "OK";
    }

    @GET
    @Path("find")
    @Produces(MediaType.APPLICATION_JSON)
    public JsonObject find() {
        return new JsonObject(Long.valueOf(1), 2, "string",
                LocalDateTime.now(), LocalTime.now(), Boolean.FALSE, Double.valueOf(1.2));
    }

}
