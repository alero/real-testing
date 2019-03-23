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

package org.hrodberaht.injection.extensions.plugin.junit.datasource.test;

import org.hrodberaht.injection.extensions.plugin.junit.datasource.service.UserService;
import org.hrodberaht.injection.extensions.plugin.junit.datasource.test.config.SQLLoadingContainerConfig;
import org.hrodberaht.injection.plugin.junit.ContainerContext;
import org.hrodberaht.injection.plugin.junit.JUnit4Runner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

@ContainerContext(SQLLoadingContainerConfig.class)
@RunWith(JUnit4Runner.class)
@Ignore
public class TestFutureUsageOnDataSourceUser {

    @Inject
    private UserService userService;

    @Test
    public void testForFuture1() throws Exception {
        testForLoginCount();
    }

    @Test
    public void testForFuture2() throws Exception {
        testForLoginCount();
    }

    private void testForLoginCount() throws ExecutionException, InterruptedException {
        Future<Integer> future = CompletableFuture.supplyAsync(() -> userService.loginCount("dude"));
        int count = future.get();
        assertEquals(1, count);
    }
}
