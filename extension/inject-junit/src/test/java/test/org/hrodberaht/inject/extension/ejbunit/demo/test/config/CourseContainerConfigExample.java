package test.org.hrodberaht.inject.extension.ejbunit.demo.test.config;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.extension.tdd.ejb.TDDEJBContainerConfigBase;

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
                "CourseContainerConfigExample", "MyDataSource", "test/org/hrodberaht/inject/extension/course");
    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.ejbunit.demo.service");
    }

}
