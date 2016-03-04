package test.com.hrodberaht.inject.extension.jsf;

import com.sun.faces.spi.InjectionProvider;
import com.sun.faces.spi.InjectionProviderException;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterJava;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Injection Extension Web
 *
 * @author Robert Alexandersson
 *         2010-jul-28 01:30:53
 * @version 1.0
 * @since 1.0
 */
public class InjectionWebInContainerTest {


    @BeforeClass
    public static void beforeClass() {
        
    }

    @Test
    public void testJsfInjectionContainerProgrammaticRegistration() throws InjectionProviderException {

        // We can not force the registration to happen before startup so this must be done statically
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerJavaXInject();
        registerJava.register(ServiceInject.class, ServiceInjectBean.class);
        InjectInjectContainer container = registerJava.getInjectContainer();
        com.hrodberaht.inject.extension.jsf.JsfInjectionProvider.setInjector(container);

        // This is basically what JSF will do
        InjectionProvider injectionProvider = new com.hrodberaht.inject.extension.jsf.JsfInjectionProvider();
        WebInjectBean bean = new WebInjectBean();
        injectionProvider.inject(bean);

        assertNotNull(bean.getServiceInject());

        assertTrue(bean.getServiceInject() instanceof ServiceInjectBean);
    }

    @Test
    public void testJsfInjectionContainerBaseClassInheritance() throws InjectionProviderException {

 
        // This is basically what JSF will do
        InjectionProvider injectionProvider = new JsfInjectionProvider();
        WebInjectBean bean = new WebInjectBean();        
        injectionProvider.inject(bean);

        assertNotNull(bean.getServiceInject());

        assertTrue(bean.getServiceInject() instanceof ServiceInjectBean);
    }

}
