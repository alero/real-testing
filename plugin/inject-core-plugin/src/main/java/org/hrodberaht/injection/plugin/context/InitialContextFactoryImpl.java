package org.hrodberaht.injection.plugin.context;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

/**
 * @author Robert Alexandersson
 */
public class InitialContextFactoryImpl implements InitialContextFactory {

    private static Context context = new ContextImpl();

    public Context getInitialContext(Hashtable<?, ?> environment) throws NamingException {
        return context;
    }
}
