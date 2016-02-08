package test.org.hrodberaht.inject.extension.ejbunit.cdi.config;

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
public class CDIContainerConfigExample extends TDDCDIContainerConfigBase {

    public CDIContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        String schemaName = "example-jpa";
        DataSource dataSource = createDataSource(dataSourceName);
        // EntityManager resource
        // EntityManager entityManager = createEntityManager(schemaName, dataSourceName, dataSource);
        // addPersistenceContext(schemaName, entityManager);
        // Named resource
        addResource(dataSourceName, dataSource);
        addSQLSchemas(
                "EJBContainerConfigExample", "MyDataSource", "test/org/hrodberaht/inject/extension/ejbunit");
        // Typed resource
        addResource(DataSource.class, dataSource);

    }


    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.ejbunit.ejb3.service");
    }


}
