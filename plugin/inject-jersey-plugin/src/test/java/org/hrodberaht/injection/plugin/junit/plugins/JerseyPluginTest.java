package org.hrodberaht.injection.plugin.junit.plugins;


import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.plugin.junit.jersey.JerseyTestBuilder;
import org.hrodberaht.injection.plugin.junit.plugins.config.ContainerConfigExample;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@ContainerContext(ContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
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
}