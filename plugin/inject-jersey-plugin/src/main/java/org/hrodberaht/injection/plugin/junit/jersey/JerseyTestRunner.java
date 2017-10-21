package org.hrodberaht.injection.plugin.junit.jersey;

import org.glassfish.jersey.test.JerseyTest;

public class JerseyTestRunner {

    private final JerseyTest jerseyTest;

    public void initializeJersey() {
        try {
            jerseyTest.setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdownJersey() {
        try {
            jerseyTest.tearDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JerseyTestRunner(JerseyTest jerseyTest) {
        this.jerseyTest = jerseyTest;
    }

    public JerseyTest getJerseyTest() {
        return jerseyTest;
    }
}
