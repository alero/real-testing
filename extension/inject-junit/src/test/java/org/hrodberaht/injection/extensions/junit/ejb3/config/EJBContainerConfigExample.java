package org.hrodberaht.injection.extensions.junit.ejb3.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.ejb.TDDEJBContainerConfigBase;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class EJBContainerConfigExample extends TDDEJBContainerConfigBase {

    public EJBContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        String schemaName = "example-jpa";
        DataSource dataSource = createDataSource(dataSourceName);
        // EntityManager resource
        EntityManager entityManager = createEntityManager(schemaName, dataSourceName, dataSource);
        addPersistenceContext(schemaName, entityManager);
        // Named resource
        addResource(dataSourceName, dataSource);
        addSQLSchemas(
                "EJBContainerConfigExample", "MyDataSource",
                "org/hrodberaht/injection/extensions/junit");
        // Typed resource
        addResource(DataSource.class, dataSource);

    }


    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.junit.ejb3.service");
    }


}
