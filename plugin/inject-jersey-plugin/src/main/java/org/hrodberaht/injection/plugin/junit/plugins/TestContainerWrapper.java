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

package org.hrodberaht.injection.plugin.junit.plugins;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.spi.TestContainer;

import java.net.URI;

class TestContainerWrapper implements TestContainer {

    private TestContainer innerContainer;

    TestContainerWrapper(TestContainer innerContainer) {
        this.innerContainer = innerContainer;
    }

    @Override
    public ClientConfig getClientConfig() {
        ClientConfig clientConfig = innerContainer.getClientConfig();
        if (clientConfig != null) {
            return clientConfig;
        }
        return new ClientConfig();
    }

    @Override
    public URI getBaseUri() {
        return innerContainer.getBaseUri();
    }

    @Override
    public void start() {
        innerContainer.start();
    }

    @Override
    public void stop() {
        innerContainer.stop();
    }
}
