import demo.service.Service;
import demo.service.ServiceBean;
import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.JUnitRunner;
import org.hrodberaht.injection.extensions.junit.util.ContainerLifeCycleTestUtil;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

@ContainerContext(JUnitModuleConfig.class)
@RunWith(JUnitRunner.class)
public class TestCDIMockedService {

    @Inject
    private ContainerLifeCycleTestUtil lifeCycleTestUtil;

    @Inject
    private Service aService;

    @Test
    public void testDoing() {
        assertEquals("done", aService.doIt());
    }


    @Test
    public void testDoingWithDeepMock() {

        lifeCycleTestUtil.registerServiceInstance(ServiceBean.class, new ServiceBean() {
            @Override
            public String doIt() {
                return "mocked done";
            }
        });

        assertEquals("mocked done", lifeCycleTestUtil.getService(Service.class).doIt());
    }

}
