package test.org.hrodberaht.inject.extension.ejbunit.spring.config;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.extension.tdd.spring.SpringContainerConfigBase;

import javax.sql.DataSource;

/**
 * Unit Test JUnit (using @Inject)
 *
 * @author Robert Alexandersson
 *         2010-okt-11 19:37:42
 * @version 1.0
 * @since 1.0
 */
public class SpringContainerConfigExample extends SpringContainerConfigBase {

    public SpringContainerConfigExample() {
        String dataSourceName = "DataSource";
        if (!hasDataSource(dataSourceName)) {
            addResource(DataSource.class, createDataSource(dataSourceName));
            addSQLSchemas(dataSourceName, "test/org/hrodberaht/inject/extension/ejb2unit");
        }
    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("test.org.hrodberaht.inject.extension.ejbunit.spring.service");
    }


}
