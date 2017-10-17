package org.hrodberaht.injection.extensions.plugin.test.spi;

public interface RunnerPlugin extends Plugin{

    void beforeContainerCreation();
    void afterContainerCreation();
    void beforeMethod();
    void afterMethod();

}
