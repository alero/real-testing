package org.hrodberaht.injection.extensions.plugin.junit.spi;

public interface RunnerPlugin extends Plugin{

    void beforeContainerCreation();
    void afterContainerCreation();
    void beforeMethod();
    void afterMethod();

}
