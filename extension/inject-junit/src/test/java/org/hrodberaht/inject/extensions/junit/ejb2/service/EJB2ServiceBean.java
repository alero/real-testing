package org.hrodberaht.inject.extensions.junit.ejb2.service;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 21:39:30
 * @version 1.0
 * @since 1.0
 */
public class EJB2ServiceBean implements SessionBean {

    private SessionContext sessionContext;


    public String getSomething(Long id) {
        return "something " + id;
    }

    public SessionContext getSessionContext() {
        return sessionContext;
    }

    public String getSomethingDeep(Long id) throws CreateException, RemoteException {

        EJB2InnerServiceLocalHome localHome = EJBHomeFactory.getInstance().getLocalHome(EJB2InnerServiceLocalHome.class);
        EJB2InnerServiceLocal local = localHome.create();
        return local.getSomething(id);
    }

    public void setSessionContext(SessionContext sessionContext) throws EJBException, RemoteException {
        this.sessionContext = sessionContext;
    }

    public void ejbRemove() throws EJBException, RemoteException {
    }

    public void ejbActivate() throws EJBException, RemoteException {
    }

    public void ejbPassivate() throws EJBException, RemoteException {
    }
}
