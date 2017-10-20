package org.hrodberaht.injection.extensions.junit.demo2.test.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.ejb.TDDEJBContainerConfigBase;

import javax.sql.DataSource;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class Course2ContainerConfigExample extends TDDEJBContainerConfigBase {

    public Course2ContainerConfigExample() {

        String dataSourceName = "MyDataSource";
        DataSource dataSource = super.createDataSource(dataSourceName);
        super.addResource(dataSourceName, dataSource);

        super.addSQLSchemas(
                "Course2ContainerConfigExample", "MyDataSource", "org/hrodberaht/injection/extensions/course2"
        );
    }

    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.junit.demo2.service");
    }

}
