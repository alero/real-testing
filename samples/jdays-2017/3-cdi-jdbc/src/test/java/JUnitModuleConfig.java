import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerCreator;
import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagedTesting;
import com.hrodberaht.inject.extensions.transaction.manager.JdbcModule;
import com.hrodberaht.inject.extensions.transaction.manager.internal.TransactionLogging;
import config.TDDCDIContainerConfigBase;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.cdi.CDIApplication;

import javax.sql.DataSource;


public class JUnitModuleConfig extends TDDCDIContainerConfigBase implements InjectionContainerCreator, TransactionManagedTesting {

    private CDIApplication cdiApplication = null;

    public JUnitModuleConfig() {
        String name = "jdbc/MyDatasource";
        DataSource dataSource = createDataSource(name);
        addResource(name, dataSource);

        cdiApplication = new CDIApplication(this);
        cdiApplication.add( new JdbcModule(dataSource));
        super.addSQLSchemas(name, "sql");
        TransactionLogging.enableLogging = true;

    }

    @Override
    public InjectContainer createContainer() {
        return cdiApplication.createContainer();
    }
}
