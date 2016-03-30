package org.hrodberaht.injection.extensions.junit.ejb.internal;

import javax.naming.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-01-20 13:26
 * @created 1.0
 * @since 1.0
 */
public class ContextImpl implements Context {

    // Map<Name, Object> typedContextItems = new HashMap<Name, Object>();
    Map<String, Object> namedContextItems = new HashMap<String, Object>();

    public Object lookup(Name name) throws NamingException {
        return namedContextItems.get(name.get(0));
    }

    public Object lookup(String name) throws NamingException {
        return namedContextItems.get(name);
    }

    public void bind(Name name, Object obj) throws NamingException {
        namedContextItems.put(name.get(0), obj);
    }

    public void bind(String name, Object obj) throws NamingException {
        namedContextItems.put(name, obj);
    }

    public void rebind(Name name, Object obj) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rebind(String name, Object obj) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void unbind(Name name) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void unbind(String name) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rename(Name oldName, Name newName) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void rename(String oldName, String newName) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public NamingEnumeration<NameClassPair> list(Name name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NamingEnumeration<NameClassPair> list(String name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NamingEnumeration<Binding> listBindings(Name name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NamingEnumeration<Binding> listBindings(String name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroySubcontext(Name name) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void destroySubcontext(String name) throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public Context createSubcontext(Name name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Context createSubcontext(String name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object lookupLink(Name name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object lookupLink(String name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NameParser getNameParser(Name name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public NameParser getNameParser(String name) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Name composeName(Name name, Name prefix) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public String composeName(String name, String prefix) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object addToEnvironment(String propName, Object propVal) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Object removeFromEnvironment(String propName) throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Hashtable<?, ?> getEnvironment() throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void close() throws NamingException {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public String getNameInNamespace() throws NamingException {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
