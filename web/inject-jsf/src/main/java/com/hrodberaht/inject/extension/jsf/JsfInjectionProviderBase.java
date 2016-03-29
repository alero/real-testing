package com.hrodberaht.inject.extensions.jsf;

import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import com.sun.faces.vendor.WebContainerInjectionProvider;
import org.hrodberaht.injection.InjectContainer;

/**
 * Injection Extension Web
 *
 * @author Robert Alexandersson
 *         2010-jul-26 22:58:42
 * @version 1.0
 * @since 1.0
 */
public abstract class JsfInjectionProviderBase implements InjectionProvider
{
    public JsfInjectionProviderBase()
    {
        System.out.println( "creating JsfInjectionProvider for com.sun.faces.spi.InjectionProvider " );
        injector = getContainer();
        System.out.println( "customer Injector loaded " );
    }



    /**
     * default injector provided by the web container.
     */
    private static final WebContainerInjectionProvider con = new WebContainerInjectionProvider();

    /**
     * Custom injector that will load our module.
     */
    protected InjectContainer injector = null;

    public abstract InjectContainer getContainer();

    public void inject( Object managedBean ) throws InjectionProviderException
    {
        // allow the default injector to inject the bean.
        con.inject( managedBean );
        // then inject with the InjectionContainer dependency injector.
        if(injector != null){
            injector.injectDependencies( managedBean );
        } else {
             System.out.println( "InjectContainer has not been setup " );    
        }
    }


    public void invokePostConstruct( Object managedBean )
            throws InjectionProviderException
    {
        // don't do anything here for the container, just let the default do its thing
        con.invokePostConstruct( managedBean );
    }
    
    public void invokePreDestroy( Object managedBean ) throws
            InjectionProviderException
    {
        con.invokePreDestroy( managedBean );
    }

}
