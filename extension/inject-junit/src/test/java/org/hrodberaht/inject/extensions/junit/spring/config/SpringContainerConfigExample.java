package org.hrodberaht.inject.extensions.junit.spring.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.spring.SpringContainerConfigBase;

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
        return createAutoScanContainer("org.hrodberaht.injectMethod.extensions.junit.spring.service");
    }


}
