package org.hrodberaht.injection.extensions.junit.internal;

import org.hrodberaht.injection.config.ContainerConfigBase;
import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.junit.util.EntityManagerHolder;
import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Created by alexbrob on 2016-03-01.
 */
public class JunitSQLContainerService {

    private static final Logger LOG = LoggerFactory.getLogger(JunitSQLContainerService.class);

    private ContainerConfigBase jpaContainerConfigBase;

    public JunitSQLContainerService(ContainerConfigBase jpaContainerConfigBase) {
        this.jpaContainerConfigBase = jpaContainerConfigBase;
    }

    public void addSQLSchemas(String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(jpaContainerConfigBase.getResourceCreator());
        LOG.debug("JunitSQLContainerService addSQLSchemas " + schemaName + ":" + packageBase);
        if (!sourceExecution.isInitiated(schemaName, packageBase)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }


    public void addSQLSchemas(String controllerPackageName, String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(jpaContainerConfigBase.getResourceCreator());
        LOG.debug("JunitSQLContainerService addSQLSchemas " +
                controllerPackageName + ":" + schemaName + ":" + packageBase);
        if (!sourceExecution.isInitiated(controllerPackageName, packageBase)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }

    public void addSingletonActiveEntityManagers() {
        Collection<EntityManager> entityManagers = null;//jpaContainerConfigBase.getEntityManagers();
        if(entityManagers != null) {
            jpaContainerConfigBase.getActiveRegister().register(new RegistrationModuleAnnotation() {
                                        @Override
                                        public void registrations() {
                                            register(EntityManagerHolder.class)
                                                    .scopeAs(ScopeContainer.Scope.SINGLETON)
                                                    .registerTypeAs(InjectionContainerManager.RegisterType.FINAL)
                                                    .withInstance(new EntityManagerHolder(entityManagers));
                                        }
                                    }
            );
        }
    }
}
