package org.hrodberaht.injection.plugin.junit.spi;

import org.hrodberaht.injection.InjectContainer;

public interface RunnerPlugin extends Plugin{

    void beforeContainerCreation();
    void afterContainerCreation(InjectContainer injectContainer);
    void beforeMethod(InjectContainer injectContainer);
    void afterMethod(InjectContainer injectContainer);

}
