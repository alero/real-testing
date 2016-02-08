package test.org.hrodberaht.inject.extension.ejbunit.ejb2.service;

import javax.ejb.EJBLocalHome;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 21:40:53
 * @version 1.0
 * @since 1.0
 */
public interface EJB2ServiceLocalHome extends EJBLocalHome {

    EJB2ServiceLocal create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
