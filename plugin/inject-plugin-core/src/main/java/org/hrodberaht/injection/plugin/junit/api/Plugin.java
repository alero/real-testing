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

package org.hrodberaht.injection.plugin.junit.api;

public interface Plugin {

    LifeCycle getLifeCycle();

    /**
     * Determines the lifecycle of the test-resource, meaning how often it will be created and kept managed by the Plugin
     *
     * @see LifeCycle#TEST
     * means that the test-resource will be recreated each time its used by a single test,
     * it also means the NO tests at all will reuse the seame instance of the test-resource
     * @see LifeCycle#TEST_CLASS
     * means that the test-resource will be recreated for every unique testclass,
     * it also means the each test within a single test-class will reuse the seame test-resource
     * @see LifeCycle#TEST_CONFIG
     * means that the test-resource will be recreated each time a new ContainerContext class is defined and used,
     * it also means that each test and testclass that uses the same config class will reuse the same test-resource
     * @see LifeCycle#TEST_SUITE
     * means that the test-resource will never be recreated duing a testsuite execution, to get this behaviour the runner is stored in a static cache.
     * Maven and other software will sometimes re-create the entire JVM when running tests and this means that the plugin will be created several times for this type of scenario.
     */
    enum LifeCycle {
        TEST_SUITE, TEST_CLASS, TEST_CONFIG, TEST
    }

}
