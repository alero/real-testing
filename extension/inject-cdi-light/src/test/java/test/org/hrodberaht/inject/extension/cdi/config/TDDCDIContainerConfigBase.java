package test.org.hrodberaht.inject.extension.cdi.config;

import org.hrodberaht.inject.InjectContainer;
import org.hrodberaht.inject.InjectionRegisterModule;
import org.hrodberaht.inject.extension.cdi.CDIContainerConfigBase;
import org.hrodberaht.inject.extension.cdi.cdiext.CDIExtensions;
import org.hrodberaht.inject.extension.tdd.internal.DataSourceExecution;
import org.hrodberaht.inject.extension.tdd.internal.ProxyResourceCreator;
import org.hrodberaht.inject.spi.ResourceCreator;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class TDDCDIContainerConfigBase extends CDIContainerConfigBase {

    private CDIExtensions cdiExtensions = new CDIExtensions();

    public TDDCDIContainerConfigBase() {
        initiateInjectionCDI();
        //  InjectionPointFinder.setInjectionFinder(finder);
    }

    private void initiateInjectionCDI() {
        resourceCreator = new ProxyResourceCreator();

    }

    protected InjectContainer createAutoScanContainer(String... packageName) {
        InjectionRegisterModule combinedRegister = preScanModuleRegistration();
        cdiExtensions.runBeforeBeanDiscovery(combinedRegister, this);
        createAutoScanContainerRegister(packageName, combinedRegister);
        cdiExtensions.runAfterBeanDiscovery(combinedRegister, this);
        return activeRegister.getContainer();
    }

    public TDDCDIContainerConfigBase(ResourceCreator resourceCreator) {
        super(resourceCreator);
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
