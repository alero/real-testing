package org.hrodberaht.injection.extensions.junit.spi;

public interface RunnerPlugin extends Plugin{

    void beforeContainerCreation();
    void afterContainerCreation();
    void beforeMethod();
    void afterMethod();

}
