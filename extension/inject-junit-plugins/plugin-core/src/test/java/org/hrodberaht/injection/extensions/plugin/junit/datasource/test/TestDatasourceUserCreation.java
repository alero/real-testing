package org.hrodberaht.injection.extensions.plugin.junit.datasource.test;

import org.hrodberaht.injection.extensions.plugin.junit.ContainerContext;
import org.hrodberaht.injection.extensions.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.extensions.plugin.junit.datasource.service.UserService;
import org.hrodberaht.injection.extensions.plugin.junit.datasource.test.config.Course2ContainerConfigExample;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.*;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 *         2011-05-08 01:50
 * @created 1.0
 * @since 1.0
 */
@ContainerContext(Course2ContainerConfigExample.class)
@RunWith(PluggableJUnitRunner.class)
public class TestDatasourceUserCreation {


    @Inject
    private UserService userService;

    @Test
    public void testNamedDataSourceExists() throws Exception {
        assertNotNull(userService.getNamedDataSource());
    }

    @Test
    public void testTypedDataSourceExists() throws Exception {
        assertNotNull(userService.getTypedDataSource());
    }

    @Test
    public void testComparePasswordGood() throws Exception {
        assertTrue(userService.loginCompare("dude", "wheremycar"));
    }

    @Test
    public void testChangePasswordGood() throws Exception {
        assertTrue(userService.loginCompare("dude", "wheremycar"));
        userService.changePassword("dude", "itsgone");
        assertTrue(userService.loginCompare("dude", "itsgone"));
    }

    @Test
    public void testChangePasswordGoodAgain() throws Exception {
        assertTrue(userService.loginCompare("dude", "wheremycar"));
        userService.changePassword("dude", "itsgone2");
        assertTrue(userService.loginCompare("dude", "itsgone2"));
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
