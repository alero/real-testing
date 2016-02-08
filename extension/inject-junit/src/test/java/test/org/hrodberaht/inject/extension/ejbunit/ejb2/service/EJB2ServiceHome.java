package test.org.hrodberaht.inject.extension.ejbunit.ejb2.service;

import javax.ejb.EJBHome;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 22:04:24
 * @version 1.0
 * @since 1.0
 */
public interface EJB2ServiceHome extends EJBHome {
    EJB2ServiceRemote create() throws javax.ejb.CreateException, java.rmi.RemoteException;
}
