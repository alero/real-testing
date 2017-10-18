package org.hrodberaht.injection.extensions.plugin.junit.demo2.test;

import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.PluggableJUnitRunner;
import org.hrodberaht.injection.extensions.plugin.junit.demo2.service.UserService;
import org.hrodberaht.injection.extensions.plugin.junit.demo2.test.config.Course2ContainerConfigExample;
import org.junit.Test;
import org.junit.runner.RunWith;


import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
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
@RunWith(PluggableJUnitRunner.class)
public class TestPlainUserCreation {


    @Inject
    private UserService userService;

    @Test
    public void testNamedResourceExists() throws Exception {
        assertEquals("named", userService.getMyNamedResource().value());
    }

    @Test
    public void testMappedResourceExists() throws Exception {
        assertEquals("mapped", userService.getMyMappedNamedResource().value());
    }


    @Test
    public void testTypedResourceExists() throws Exception {
        assertEquals("typed", userService.getMyTypedResource().value());
    }

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
