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

package org.hrodberaht.injection.plugin.junit.inner;

import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;

import java.lang.annotation.Annotation;
import java.util.Map;

public abstract class PluginRunnerBase implements RunnerPluginInterface {

    private final AnnotatedRunnerPlugin annotatedPluginRunner;
    private final Map<Class<? extends Plugin>, Plugin> activePlugins;

    public PluginRunnerBase(Map<Class<? extends Plugin>, Plugin> activePlugins,
                            AnnotatedRunnerPlugin annotatedPluginRunner) {
        this.activePlugins = activePlugins;
        this.annotatedPluginRunner = annotatedPluginRunner;
    }

    @Override
    public Plugin addAnnotatedPlugin(Plugin plugin) {
        return annotatedPluginRunner.addPlugin(plugin);
    }


    interface RunnerPluginOp {
        void runOp();
    }

    void runIfActive(Class pluginClass, RunnerPluginOp runnerPluginOp) {
        if (activePlugins.get(pluginClass) != null) {
            runnerPluginOp.runOp();
        }
    }


    @Override
    public void findAnnotationAndInvokeMethod(Class pluginClass, PluginContext pluginContext, Class<Annotation> annotation) {
        annotatedPluginRunner.findAnnotationAndInvokeMethod(pluginClass, this, pluginContext, annotation);
    }
}
