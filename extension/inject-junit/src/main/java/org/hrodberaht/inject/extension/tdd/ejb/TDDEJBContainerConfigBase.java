package org.hrodberaht.inject.extension.tdd.ejb;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.extension.tdd.ejb.internal.InjectionRegisterScanEJB;
import org.hrodberaht.inject.extension.tdd.ejb.internal.SessionContextCreator;
import org.hrodberaht.inject.extension.tdd.internal.TDDContainerConfigBase;
import org.hrodberaht.inject.internal.annotation.DefaultInjectionPointFinder;
import org.hrodberaht.inject.register.InjectionRegister;
import org.hrodberaht.inject.spi.module.CustomInjectionPointFinderModule;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.SessionBean;
import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.HashMap;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class TDDEJBContainerConfigBase extends TDDContainerConfigBase<InjectionRegisterScanEJB> {



    protected TDDEJBContainerConfigBase() {
    }

    @Override
    protected InjectionRegisterModule preScanModuleRegistration() {
        InjectionRegisterModule injectionRegisterModule = new InjectionRegisterModule();
        injectionRegisterModule.register(new CustomInjectionPointFinderModule(
                new DefaultInjectionPointFinder(this) {
                    @Override
                    protected boolean hasInjectAnnotationOnMethod(Method method) {
                        return super.hasInjectAnnotationOnMethod(method);
                    }

                    @Override
                    protected boolean hasInjectAnnotationOnField(Field field) {
                        return field.isAnnotationPresent(EJB.class) ||
                                super.hasInjectAnnotationOnField(field);
                    }

                    @Override
                    protected boolean hasPostConstructAnnotation(Method method) {
                        return method.isAnnotationPresent(PostConstruct.class) ||
                                super.hasPostConstructAnnotation(method);
                    }

                    @Override
                    public void extendedInjection(Object service) {
                        TDDEJBContainerConfigBase config = (TDDEJBContainerConfigBase) getContainerConfig();
                        config.injectResources(service);
                    }
                }
        ));


        return injectionRegisterModule;
    }



    public abstract InjectContainer createContainer();

    @Override
    protected InjectionRegisterScanEJB getScanner(InjectionRegister registerModule) {
        return new InjectionRegisterScanEJB(registerModule);
    }

    protected void addPersistenceContext(String name, EntityManager entityManager) {
        if (entityManagers == null) {
            entityManagers = new HashMap<>();
        }
        entityManagers.put(name, entityManager);
    }




    protected void injectResources(Object serviceInstance) {
        if (serviceInstance instanceof SessionBean) {
            SessionBean sessionBean = (SessionBean) serviceInstance;
            try {
                sessionBean.setSessionContext(SessionContextCreator.create());
            } catch (RemoteException e) {
                // Nope
            }
        }

        injectGenericResources(serviceInstance);
    }






}
