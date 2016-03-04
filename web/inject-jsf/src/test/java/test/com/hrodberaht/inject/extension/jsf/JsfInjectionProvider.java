package test.com.hrodberaht.inject.extension.jsf;

import com.hrodberaht.inject.extension.jsf.JsfInjectionProviderBase;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterJava;

/**
 * Injection Extension Web
 *
 * @author Robert Alexandersson
 *         2010-jul-26 22:58:42
 * @version 1.0
 * @since 1.0
 */
public class JsfInjectionProvider extends JsfInjectionProviderBase{

    private static InjectInjectContainer container;

    static{
        InjectionRegisterJava registerJava = new InjectionRegisterJava();
        registerJava.activateContainerJavaXInject();
        registerJava.register(ServiceInject.class, ServiceInjectBean.class);
        container = registerJava.getInjectContainer();
    }

    /**
     * Do not create the container here,
     * as it's the Constructor of the Base class that calls the InjectContainer getContainer
     */
    public JsfInjectionProvider() {
    }

    @Override
    public InjectContainer getContainer() {
        return container;
    }


}
