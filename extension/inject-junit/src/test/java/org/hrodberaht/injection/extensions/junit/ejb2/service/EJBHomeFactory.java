package org.hrodberaht.injection.extensions.junit.ejb2.service;

import javax.ejb.EJBHome;
import javax.ejb.EJBLocalHome;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 21:53:38
 * @version 1.0
 * @since 1.0
 */
public class EJBHomeFactory {
    private static EJBHomeFactory instance;

    public static EJBHomeFactory getInstance() {
        return instance;
    }

    public static void setInstance(EJBHomeFactory instance) {
        EJBHomeFactory.instance = instance;
    }

    private String getLocalHomeName(Class localHomeClass) {
        String name = localHomeClass.getSimpleName();
        name = name.substring(0, name.indexOf("LocalHome"));
        return name + "Local";
    }

    public <T extends EJBLocalHome> T getLocalHome(Class<T> localHomeClass) {
        try {
            Context context = new InitialContext();
            return (T) context.lookup("java:comp/env/ejb/" + getLocalHomeName(localHomeClass));
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    public <T extends EJBHome> T getHome(Class<T> homeClass) {
        try {
            Context context = new InitialContext();
            Object o = context.lookup("java:comp/env/ejb/" + getLocalHomeName(homeClass));
            return (T) PortableRemoteObject.narrow(o, homeClass);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
