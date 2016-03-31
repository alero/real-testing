package test.org.hrodberaht.inject.extension.cdi.config;

import org.hrodberaht.injection.extensions.cdi.CDIContainerConfigBase;
import org.hrodberaht.injection.extensions.junit.internal.DataSourceExecution;
import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.spi.ResourceCreator;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class TDDCDIContainerConfigBase extends CDIContainerConfigBase {

    public TDDCDIContainerConfigBase(ResourceCreator resourceCreator) {
        super(resourceCreator);
    }

    public TDDCDIContainerConfigBase() {
        initiateInjectionCDI();
    }

    private void initiateInjectionCDI() {
        resourceCreator = new ProxyResourceCreator();
    }

    public void addSQLSchemas(String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(resourceCreator);
        if (!sourceExecution.isInitiated(schemaName, schemaName)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }

    public void addSQLSchemas(String testPackageName, String schemaName, String packageBase) {
        DataSourceExecution sourceExecution = new DataSourceExecution(resourceCreator);
        if (!sourceExecution.isInitiated(testPackageName, schemaName)) {
            sourceExecution.addSQLSchemas(schemaName, packageBase);
        }
    }


}
