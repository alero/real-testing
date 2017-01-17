package org.hrodberaht.injection.extensions.junit.demo.test.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.ejb.TDDEJBContainerConfigBase;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 *         2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class CourseContainerConfigExample extends TDDEJBContainerConfigBase {

    public CourseContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        String jpaName = "example-jpa";
        DataSource dataSource = createDataSource(dataSourceName);
        // EntityManager resource
        EntityManager entityManager = createEntityManager(jpaName, dataSourceName, dataSource);
        addPersistenceContext(jpaName, entityManager);

        addSQLSchemas(
                "CourseContainerConfigExample", "MyDataSource", "org/hrodberaht/injection/extensions/course");
    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.junit.demo.service");
    }

}
