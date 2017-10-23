package config;

import org.hrodberaht.injection.extensions.cdi.CDIContainerConfigBase;
import org.hrodberaht.injection.extensions.junit.internal.JunitSQLContainerService;
import org.hrodberaht.injection.extensions.junit.internal.ProxyResourceCreator;
import org.hrodberaht.injection.core.spi.ResourceCreator;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 10:37
 * To change this template use File | Settings | File Templates.
 */
public abstract class TDDCDIContainerConfigBase extends CDIContainerConfigBase {

    private JunitSQLContainerService junitSQLContainerService = new JunitSQLContainerService(this);

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
        junitSQLContainerService.addSQLSchemas(schemaName, packageBase);
    }

    public void addSQLSchemas(String testPackageName, String schemaName, String packageBase) {
        junitSQLContainerService.addSQLSchemas(testPackageName, schemaName, packageBase);
    }


}
