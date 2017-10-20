package org.hrodberaht.injection.extensions.junit.ejb2.config;

import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.extensions.junit.ejb.TDDEJBContainerConfigBase;

/**
 * Injection Extension JUnit
 *
 * @author Robert Alexandersson
 * 2010-okt-26 20:49:29
 * @version 1.0
 * @since 1.0
 */
public class EJBContainerConfigExample extends TDDEJBContainerConfigBase {

    public EJBContainerConfigExample() {

        String dataSourceName = "DataSource";
        if (!hasDataSource(dataSourceName)) {
            addResource(dataSourceName, createDataSource(dataSourceName));
            addSQLSchemas(dataSourceName, "org/hrodberaht/injection/extensions/junit/ejb2unit");
        }
    }


    @Override
    public InjectContainer createContainer() {
        return createAutoScanContainer("org.hrodberaht.injection.extensions.junit.ejb3.service");
    }


}
