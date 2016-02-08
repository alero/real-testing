package test.org.hrodberaht.inject.extension.cdi.config;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.extension.tdd.cdi.TDDCDIContainerConfigBase;

import javax.sql.DataSource;

/**
 * Unit Test EJB (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleForJSEResourceCreator extends TDDCDIContainerConfigBase {

    public CDIContainerConfigExampleForJSEResourceCreator() {

        // super(new JSEResourceCreator());

        System.setProperty("MyDataSource.driver", "org.hsqldb.jdbcDriver");
        System.setProperty("MyDataSource.url", "jdbc:hsqldb:mem:MyDataSource");
        System.setProperty("MyDataSource.username", "sa");
        System.setProperty("MyDataSource.password", "");

        String dataSourceName = "MyDataSource";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);
        addSQLSchemas(dataSourceName, "test");

    }


    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.cdi.service2");
    }


}
