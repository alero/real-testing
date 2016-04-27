package org.hrodberaht.injection.extensions.junit.internal;

import org.hrodberaht.injection.config.jpa.JPAContainerConfigBase;
import org.hrodberaht.injection.extensions.junit.util.EntityManagerHolder;
import org.hrodberaht.injection.internal.InjectionContainerManager;
import org.hrodberaht.injection.internal.ScopeContainer;
import org.hrodberaht.injection.register.RegistrationModuleAnnotation;

import javax.persistence.EntityManager;
import java.util.Collection;

/**
 * Created by alexbrob on 2016-03-01.
 */
public class JunitSQLContainerService {

    private JPAContainerConfigBase jpaContainerConfigBase;

    public JunitSQLContainerService(JPAContainerConfigBase jpaContainerConfigBase) {
        this.jpaContainerConfigBase = jpaContainerConfigBase;
    }

    public void addSQLSchemas(String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(jpaContainerConfigBase.getResourceCreator());
        if (!sourceExecution.isInitiated(schemaName, packageBase)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }


    public void addSQLSchemas(String controllerPackageName, String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(jpaContainerConfigBase.getResourceCreator());
        if (!sourceExecution.isInitiated(controllerPackageName, packageBase)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }

    public void addSingletonActiveEntityManagers() {
        Collection<EntityManager> entityManagers = jpaContainerConfigBase.getEntityManagers();
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
