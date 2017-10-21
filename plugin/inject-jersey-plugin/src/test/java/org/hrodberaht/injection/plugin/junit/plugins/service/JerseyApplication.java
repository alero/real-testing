package org.hrodberaht.injection.plugin.junit.plugins.service;


import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.util.logging.Logger;

public class JerseyApplication extends ResourceConfig {


    private static final java.util.logging.Logger LOGGER = Logger.getLogger(JerseyApplication.class.getName());

    public JerseyApplication() {
        super();
        setApplicationName("Seal Blobstore API");
        register(ObjectMapperResolver.class);

        register(new LoggingFeature(LOGGER, LoggingFeature.Verbosity.HEADERS_ONLY));
        packages(true, "org.hrodberaht.injection.plugin.junit.plugins.resource");
    }


}
