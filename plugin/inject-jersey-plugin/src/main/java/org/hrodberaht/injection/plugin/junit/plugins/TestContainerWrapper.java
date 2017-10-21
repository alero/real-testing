package org.hrodberaht.injection.plugin.junit.plugins;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.spi.TestContainer;

import java.net.URI;

/**
 * Created by robertalexandersson on 16/05/16.
 */
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
