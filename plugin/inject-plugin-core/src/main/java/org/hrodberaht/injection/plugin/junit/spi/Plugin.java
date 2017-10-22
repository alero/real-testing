package org.hrodberaht.injection.plugin.junit.spi;

public interface Plugin {

    /**
     * Determines the lifecycle of the plugin, meaning how often it will be created and kept managed by the JUnitRunner
     * @see Plugin.LifeCycle#TEST_CLASS
     * means that the plugin will be recreated each time its used by a testclass,
     * it also means the each test within a class will reuse the seame plugin instance
     * @see Plugin.LifeCycle#TEST_CONFIG
     * means that the plugin will be recreated each time a new ContainerContext class is defined and used,
     * it also means that each test and testclass that uses the same config class will reuse the same plugin instance
     * @see Plugin.LifeCycle#TEST_SUITE
     * means that the plugin will never be recreated duing a testsuite execution, do get this behaviour the runner is stored in a static cache.
     * Maven and other softwar will sometimes re-create the entire JVM when running tests and this means that the plugin will be created several times for this type of scenario.
     * @return the lifecycle the plugin should utilize
     */
    LifeCycle getLifeCycle();

    enum LifeCycle {TEST_SUITE, TEST_CLASS, TEST_CONFIG}

}
