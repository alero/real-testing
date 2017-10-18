package org.hrodberaht.injection.plugin.context;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class ContextManager {
    public ContextManager() {
        System.setProperty(Context.INITIAL_CONTEXT_FACTORY, InitialContextFactoryImpl.class.getName());
    }

    public void bind(String name, Object instance) {
        try {
            Context context = new InitialContext();
            context.bind(name, instance);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }
}
