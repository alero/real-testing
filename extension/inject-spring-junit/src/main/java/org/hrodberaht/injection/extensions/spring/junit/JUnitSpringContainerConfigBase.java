package org.hrodberaht.injection.extensions.spring.junit;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.internal.JunitSQLContainerService;
import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.extensions.spring.SpringContainerConfigBase;
import org.hrodberaht.injection.spi.ResourceCreator;

/**
 * Created by robertalexandersson on 4/15/16.
 */
public abstract class JUnitSpringContainerConfigBase extends SpringContainerConfigBase {

    protected JunitSQLContainerService junitSQLContainerService;

    public JUnitSpringContainerConfigBase(ResourceCreator resourceCreator) {
        super(resourceCreator);
        junitSQLContainerService = new JunitSQLContainerService(this);
    }

    public JUnitSpringContainerConfigBase() {
        junitSQLContainerService = new JunitSQLContainerService(this);
    }

    @Override
    public void addSingletonActiveRegistry() {
        junitSQLContainerService.addSingletonActiveEntityManagers();
        super.addSingletonActiveRegistry();
    }

    @Override
    public InjectContainer createContainer() {
        return createEmptyContainer();
    }

    @Override
    protected ResourceCreator createResourceCreator() {
        return new ProxyResourceCreator();
    }

}
