package test.com.hrodberaht.inject.extension.transaction.example;

import com.hrodberaht.inject.extension.transaction.junit.InjectionContainerCreator;
import com.hrodberaht.inject.extension.transaction.junit.TransactionManagedTesting;
import com.hrodberaht.inject.extension.transaction.manager.JpaModule;
import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterModule;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class ModuleContainerForTests implements InjectionContainerCreator, TransactionManagedTesting {

    public static InjectContainer container;
    static {
        InjectionRegisterModule register = new InjectionRegisterModule();
        register.register(TransactedApplication.class,  JPATransactedApplication.class);

        register.register(new JpaModule("example-jpa"));
        InjectContainer injectContainer = register.getContainer();
        container = injectContainer;
    }

    public InjectContainer createContainer() {
        return container;
    }
}