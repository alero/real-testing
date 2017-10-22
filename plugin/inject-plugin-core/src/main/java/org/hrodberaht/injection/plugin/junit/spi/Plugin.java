package org.hrodberaht.injection.plugin.junit.spi;

public interface Plugin {

    LifeCycle getLifeCycle();

    enum LifeCycle {TESTSUITE, TESTCLASS, TEST}

}
