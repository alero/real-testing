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
