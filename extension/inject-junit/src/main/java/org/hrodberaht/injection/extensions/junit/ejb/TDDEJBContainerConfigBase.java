package org.hrodberaht.injection.extensions.junit.ejb;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.ejb.internal.InjectionRegisterScanEJB;
import org.hrodberaht.injection.extensions.junit.ejb.internal.SessionContextCreator;
import org.hrodberaht.injection.extensions.junit.internal.JunitContainerConfigBase;
import org.hrodberaht.injection.internal.InjectionRegisterModule;
import org.hrodberaht.injection.register.InjectionRegister;

import javax.ejb.SessionBean;
import javax.persistence.EntityManager;
import java.rmi.RemoteException;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:35:27
 * @version 1.0
 * @since 1.0
 */
public abstract class TDDEJBContainerConfigBase extends JunitContainerConfigBase<InjectionRegisterScanEJB> {

    protected TDDEJBContainerConfigBase() {
    }

    @Override
    protected InjectionRegisterModule preScanModuleRegistration() {
        return EJBContainerConfigUtil.createEJBInjectionModule(this);
    }

    public abstract InjectContainer createContainer();

    @Override
    protected InjectionRegisterScanEJB getScanner(InjectionRegister registerModule) {
        return new InjectionRegisterScanEJB(registerModule);
    }

    public void addPersistenceContext(String name, EntityManager entityManager) {
        resourceInjection.addPersistenceContext(name, entityManager);
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

        resourceInjection.injectResources(serviceInstance);
    }






}
