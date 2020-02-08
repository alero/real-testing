package test.com.hrodberaht.inject.extension.transaction.example;

import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class ModuleContainerForSimpleTests implements com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerCreator {
    public ModuleContainerForSimpleTests() {
    }

    public InjectContainer createContainer() {
        InjectionRegisterModule register = new InjectionRegisterModule();
        return register.getContainer();
    }
}