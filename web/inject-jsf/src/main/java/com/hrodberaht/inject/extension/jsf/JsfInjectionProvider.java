package com.hrodberaht.inject.extensions.jsf;

import org.hrodberaht.injection.InjectContainer;

/**
 * Injection Extension Web
 *
 * @author Robert Alexandersson
 *         2010-jul-26 22:58:42
 * @version 1.0
 * @since 1.0
 */
public class JsfInjectionProvider extends JsfInjectionProviderBase{

    private static InjectInjectContainer container = null;
    public static void setInjector(InjectInjectContainer container) {
        JsfInjectionProvider.container = container;
    }

    @Override
    public InjectContainer getContainer() {
        return JsfInjectionProvider.container;
    }


}
