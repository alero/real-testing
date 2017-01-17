package org.hrodberaht.injection.extensions.junit.ejb2.service;

import javax.ejb.EJBLocalHome;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 21:40:53
 * @version 1.0
 * @since 1.0
 */
public interface EJB2InnerServiceLocalHome extends EJBLocalHome {

    EJB2InnerServiceLocal create() throws javax.ejb.CreateException, java.rmi.RemoteException;

}
