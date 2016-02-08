package test.org.hrodberaht.inject.extension.ejbunit.ejb2.service;

import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 21:39:30
 * @version 1.0
 * @since 1.0
 */
public class EJB2InnerServiceBean implements SessionBean {

    private SessionContext sessionContext;

    public String getSomething(Long id) {
        return "Something Deep " + id;
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
