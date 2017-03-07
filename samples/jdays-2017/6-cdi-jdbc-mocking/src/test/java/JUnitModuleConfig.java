import com.JUnitStreamCDIContainerConfig;
import org.hrodberaht.injection.extensions.cdi.stream.CDIInjectionRegistryStream;

public class JUnitModuleConfig extends JUnitStreamCDIContainerConfig {
    @Override
    protected void registerStream(CDIInjectionRegistryStream cdiInjectionRegistryStream) {
        cdiInjectionRegistryStream.scan(() -> "demo.service");
    }
}
