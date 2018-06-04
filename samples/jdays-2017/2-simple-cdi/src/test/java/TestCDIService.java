import demo.service.Service;
import demo.service.ServiceBean;
import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@ContainerContext(JUnitModuleConfig.class)
@RunWith(JUnitRunner.class)
public class TestCDIService {
    @Inject
    private Service aService;
    @Inject
    private ServiceBean serviceBean;

    @Test
    public void testDoing() {
        assertEquals("done", aService.doIt());
    }

    @Test
    public void testDoingOnBean() {
        assertEquals("done", serviceBean.doIt());
    }
}
