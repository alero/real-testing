package test.org.hrodberaht.inject.extension.ejbunit.ejb2.service;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 22:04:21
 * @version 1.0
 * @since 1.0
 */
public interface EJB2ServiceRemote extends EJBObject {
    public String getSomething(Long id) throws RemoteException;
}
