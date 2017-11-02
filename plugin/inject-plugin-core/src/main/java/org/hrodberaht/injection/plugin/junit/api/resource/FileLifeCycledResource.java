package org.hrodberaht.injection.plugin.junit.api.resource;

import org.hrodberaht.injection.plugin.junit.api.Plugin;
import org.hrodberaht.injection.plugin.junit.api.PluginContext;

public interface FileLifeCycledResource {
    String testDirectory(String base, PluginContext pluginContext, Plugin.LifeCycle lifeCycle);
}
