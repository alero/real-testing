package org.hrodberaht.injection.plugin.junit.spi;

public interface RunnerPlugin extends Plugin{

    void beforeContainerCreation();
    void afterContainerCreation();
    void beforeMethod();
    void afterMethod();

}
