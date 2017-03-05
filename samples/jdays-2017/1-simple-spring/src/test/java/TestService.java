import demo.service.Service;
import org.hrodberaht.injection.extensions.junit.ContainerContext;
import org.hrodberaht.injection.extensions.junit.SpringJUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

@ContainerContext(JUnitModuleConfig.class)
@RunWith(SpringJUnitRunner.class)
public class TestService{
    @Autowired
    private Service aService;
    @Test
    public void testDoing(){
        assertEquals("done", aService.doIt());
    }
}
