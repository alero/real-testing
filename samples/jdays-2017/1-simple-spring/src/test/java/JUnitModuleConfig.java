import demo.config.SpringConfig;
import org.hrodberaht.injection.extensions.spring.junit.JUnitSpringContainerConfigBase;

public class JUnitModuleConfig  extends JUnitSpringContainerConfigBase {

    public JUnitModuleConfig() {
        loadJavaSpringConfig(SpringConfig.class);
    }
}
