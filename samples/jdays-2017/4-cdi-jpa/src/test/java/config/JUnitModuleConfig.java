package config;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerCreator;
import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagedTesting;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.cdi.CDIContainerConfigBase;
import org.hrodberaht.injection.extensions.junit.internal.JunitSQLContainerService;
import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;

import javax.sql.DataSource;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class JUnitModuleConfig extends CDIContainerConfigBase implements InjectionContainerCreator, TransactionManagedTesting {

    private JunitSQLContainerService junitSQLContainerService = new JunitSQLContainerService(this);

    public JUnitModuleConfig() {
        this.resourceCreator = new ProxyResourceCreator();
    }

    public InjectContainer createContainer() {
        TransactionLogging.enableLogging = true;
        InjectionRegisterModule register = new InjectionRegisterModule();

        String name = "MyDataSource";
        DataSource dataSource = createDataSource(name);
        addResource(name, dataSource);

        // junitSQLContainerService.addSQLSchemas("MyDataSource", "sql");

        NewJpaModule jpaModule = new NewJpaModule("example-jpa");
        addPersistenceContext("example-jpa", jpaModule.entityManager());

        register.register(jpaModule);

        return createContainer(register);
    }


}