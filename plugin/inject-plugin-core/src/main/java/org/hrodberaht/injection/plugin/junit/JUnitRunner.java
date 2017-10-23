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

package org.hrodberaht.injection.plugin.junit;

import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

public class JUnitRunner extends BlockJUnit4ClassRunner {

    private final JUnitContext jUnitContext;

    /**
     * Creates a BlockJUnit4ClassRunner to run
     *
     * @throws InitializationError if the test class is malformed.
     */
    public JUnitRunner(final Class<?> clazz) throws InitializationError {
        super(clazz);
        jUnitContext = new JUnitContext(clazz);
    }

    /**
     * @param frameworkMethod
     * @param notifier
     */
    @Override
    protected void runChild(final FrameworkMethod frameworkMethod, final RunNotifier notifier) {

        jUnitContext.runChild(frameworkMethod, () -> super.runChild(frameworkMethod, notifier), (e) -> {
            Description description = describeChild(frameworkMethod);
            notifier.fireTestFailure(new Failure(description, e));
            notifier.fireTestFinished(description);
        });
    }


    @Override
    public void run(final RunNotifier notifier) {

        jUnitContext.run(() -> super.run(notifier));
    }

    /**
     * Runs the injection of dependencies and resources on the test case before returned
     *
     * @return the testcase
     * @throws Exception
     */
    @Override
    protected Object createTest() throws Exception {
        return jUnitContext.createTest(this::create);
    }

    private Object create() throws Exception {
        return super.createTest();
    }
}
