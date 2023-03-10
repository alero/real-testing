/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.junit.datasource.test;

import org.hrodberaht.injection.plugin.junit.datasource.service.UserService;
import org.hrodberaht.injection.plugin.junit.datasource.test.config.SQLLoadingContainerConfig;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ContainerContext(SQLLoadingContainerConfig.class)
@RunWith(JUnit4Runner.class)
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
