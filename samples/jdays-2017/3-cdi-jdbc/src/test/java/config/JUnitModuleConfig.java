package config;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerCreator;
import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagedTesting;
import com.hrodberaht.inject.extensions.transaction.manager.JdbcModule;
import org.hrodberaht.injection.InjectContainer;
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
public class JUnitModuleConfig extends TDDCDIContainerConfigBase implements InjectionContainerCreator, TransactionManagedTesting {

    public InjectContainer createContainer() {
        // TransactionLogging.enableLogging = true;
        InjectionRegisterModule register = new InjectionRegisterModule();

        String name = "jdbc/MyDatasource";
        DataSource dataSource = createDataSource(name);
        addResource(name, dataSource);

        super.addSQLSchemas(name, "sql");

        register.register(new JdbcModule(dataSource));
        return register.getContainer();
    }


}