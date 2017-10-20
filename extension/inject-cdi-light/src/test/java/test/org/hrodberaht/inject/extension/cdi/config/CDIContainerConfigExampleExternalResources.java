package test.org.hrodberaht.inject.extension.cdi.config;

import org.hrodberaht.injection.InjectContainer;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 * 2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class CDIContainerConfigExampleExternalResources extends TDDCDIContainerConfigBase {

    public CDIContainerConfigExampleExternalResources() {

        String dataSourceName = "ExampleDataSource";
        String jpaName = "example-jpa";
        DataSource dataSource = createDataSource(dataSourceName);
        // Named resource
        addResource(dataSourceName, dataSource);

        EntityManager entityManager = createEntityManager(jpaName, dataSourceName, dataSource);
        addPersistenceContext(jpaName, entityManager);

    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.cdi.example.service");
    }


}
