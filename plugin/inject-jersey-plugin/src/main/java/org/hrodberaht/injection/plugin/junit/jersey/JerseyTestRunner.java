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

package org.hrodberaht.injection.plugin.junit.jersey;

import org.glassfish.jersey.test.JerseyTest;

public class JerseyTestRunner {

    private final JerseyTest jerseyTest;

    public void initializeJersey() {
        try {
            jerseyTest.setUp();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdownJersey() {
        try {
            jerseyTest.tearDown();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public JerseyTestRunner(JerseyTest jerseyTest) {
        this.jerseyTest = jerseyTest;
    }

    public JerseyTest getJerseyTest() {
        return jerseyTest;
    }
}
