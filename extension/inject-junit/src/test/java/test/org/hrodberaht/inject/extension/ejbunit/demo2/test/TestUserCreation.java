package test.org.hrodberaht.inject.extension.ejbunit.demo2.test;

import org.hrodberaht.injection.extensions.tdd.ContainerContext;
import org.hrodberaht.injection.extensions.tdd.JUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.org.hrodberaht.inject.extension.ejbunit.demo2.service.UserService;
import test.org.hrodberaht.inject.extension.ejbunit.demo2.test.config.Course2ContainerConfigExample;

import javax.ejb.EJB;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 *         2011-05-08 01:50
 * @created 1.0
 * @since 1.0
 */
@ContainerContext(Course2ContainerConfigExample.class)
@RunWith(JUnitRunner.class)
public class TestUserCreation {


    @EJB
    private UserService userService;

    @Test
    public void testComparePasswordGood() throws Exception {
        assertTrue(userService.loginCompare("dude", "wheremycar"));
    }

    @Test
    public void testComparePasswordBad() throws Exception {
        assertFalse(userService.loginCompare("dude", "badpass"));
    }

    @Test
    public void testComparePasswordBadNoHit() throws Exception {
        assertFalse(userService.loginCompare("dude2", "wheremycar"));
    }

    @Test
    public void testComparePasswordBadNull() throws Exception {
        assertFalse(userService.loginCompare("dude", null));
    }
}
