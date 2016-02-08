package org.hrodberaht.inject.extension.tdd.cdi;

import org.hrodberaht.inject.extension.cdi.CDIContainerConfigBase;
import org.hrodberaht.inject.extension.tdd.internal.DataSourceExecution;
import org.hrodberaht.inject.extension.tdd.internal.DataSourceProxy;
import org.hrodberaht.inject.extension.tdd.internal.ProxyResourceCreator;
import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.spi.ResourceCreator;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class TDDCDIContainerConfigBase extends CDIContainerConfigBase {

    private boolean annotationForEJB = true;

    public TDDCDIContainerConfigBase() {
        initiateInjectionCDI();
        //  InjectionPointFinder.setInjectionFinder(finder);
    }

    private void initiateInjectionCDI() {
        resourceCreator = new ProxyResourceCreator();
        DefaultInjectionPointFinder finder = new DefaultInjectionPointFinder(this) {
            @Override
            protected boolean hasInjectAnnotationOnMethod(Method method) {
                try {

                    return (annotationForEJB ? method.isAnnotationPresent(EJB.class) : false)
                            || super.hasInjectAnnotationOnMethod(method);
                } catch (NoClassDefFoundError error) {
                    annotationForEJB = false;
                    return super.hasInjectAnnotationOnMethod(method);
                }
            }

            @Override
            protected boolean hasInjectAnnotationOnField(Field field) {
                try {
                    return (annotationForEJB ? field.isAnnotationPresent(EJB.class) : false) ||
                            super.hasInjectAnnotationOnField(field);
                } catch (NoClassDefFoundError error) {
                    annotationForEJB = false;
                    return super.hasInjectAnnotationOnField(field);
                }
            }

            @Override
            protected boolean hasPostConstructAnnotation(Method method) {
                return method.isAnnotationPresent(PostConstruct.class) ||
                        super.hasPostConstructAnnotation(method);
            }

            @Override
            public void extendedInjection(Object service) {
                TDDCDIContainerConfigBase config = (TDDCDIContainerConfigBase) getContainerConfig();
                if (config != null) {
                    // System.out.println("DefaultInjectionPointFinder injecting resources");
                    config.injectResources(service);
                } else {
                    System.out.println("DefaultInjectionPointFinder NOT injecting resources due to config value null");
                }
            }

        };
        super.injectionFinder = finder;
    }

    public TDDCDIContainerConfigBase(ResourceCreator resourceCreator) {
        super(resourceCreator);
    }

    public void addSQLSchemas(String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(resourceCreator);
        if (!sourceExecution.isInitiated(schemaName, schemaName)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }

    public void addSQLSchemas(String testPackageName, String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(resourceCreator);
        if (!sourceExecution.isInitiated(testPackageName, schemaName)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }

    protected void copyOriginalRegistryToActive() {
        activeRegister = originalRegister.clone();
    }

    public ResourceCreator<EntityManager, DataSourceProxy> getResourceCreator() {
        return resourceCreator;
    }

    protected DataSource createDataSource(String dataSourceName) {
        return resourceCreator.createDataSource(dataSourceName);
    }

    protected boolean hasDataSource(String dataSourceName) {
        return resourceCreator.hasDataSource(dataSourceName);
    }


}
