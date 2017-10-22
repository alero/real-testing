package org.hrodberaht.injection.plugin.junit.plugins;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnitRunner;
import org.hrodberaht.injection.plugin.junit.plugins.config.ContainerConfigExample;
import org.hrodberaht.injection.plugin.junit.plugins.resource.JsonObject;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@ContainerContext(ContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class JerseyPluginTest {

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
    public void testFind() throws Exception {

        JsonObject response = jerseyPlugin.testBuilder().given()
                .expect()
                .statusCode(200)
                .get("/api/find").response(JsonObject.class);

        assertEquals("string", response.getaString());

    }
}