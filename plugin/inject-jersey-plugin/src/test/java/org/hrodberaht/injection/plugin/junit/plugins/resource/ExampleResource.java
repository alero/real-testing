package org.hrodberaht.injection.plugin.junit.plugins.resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("api")
public class ExampleResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleResource.class);


    @GET
    @Path("isAlive")
    @Produces(MediaType.APPLICATION_JSON)
    public String isAlive() {
        return "OK";
    }



}
