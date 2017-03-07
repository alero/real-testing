import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extensions.transaction.junit.InjectionJUnitTestRunner;
import config.JUnitModuleConfig;
import demo.service.Service;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.TransactionAttribute;
import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@InjectionContainerContext(JUnitModuleConfig.class)
@RunWith(InjectionJUnitTestRunner.class)
@TransactionAttribute
public class TestCDIJDBCService {
    @Inject private Service aService;
    @Test public void testDoing(){
        aService.createData("init", "done");
        assertEquals("done", aService.findIt("init"));
    }

    @Test public void testChangingWhatIsDone(){
        aService.createData("init", "done");
        assertEquals("done", aService.findIt("init"));
        aService.changeIt("init", "changed what done");
        assertEquals("changed what done", aService.findIt("init"));
    }

    @Test public void testChangingWhatIsDoneAgain(){
        aService.createData("init", "done");
        assertEquals("done", aService.findIt("init"));
        aService.changeIt("init", "changed what done again");
        assertEquals("changed what done again", aService.findIt("init"));
    }

    @Test public void testNotFindingIt(){
        assertNull("done", aService.findIt("init"));
    }

}
