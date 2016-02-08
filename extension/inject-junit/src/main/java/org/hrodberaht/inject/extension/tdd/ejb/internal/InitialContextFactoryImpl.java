package org.hrodberaht.inject.extension.tdd.ejb.internal;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-01-20 13:25
 * @created 1.0
 * @since 1.0
 */
public class InitialContextFactoryImpl implements InitialContextFactory {

    private static Context context = new ContextImpl();

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return context;
    }
}
