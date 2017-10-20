package org.hrodberaht.injection.plugin.junit.plugins.config;

import org.hrodberaht.injection.plugin.junit.PluggableContainerConfigBase;
import org.hrodberaht.injection.plugin.junit.plugins.DataSourcePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.GuicePlugin;
import org.hrodberaht.injection.plugin.junit.plugins.service.config.GuiceModule;
import org.hrodberaht.injection.stream.InjectionRegistryBuilder;

import javax.sql.DataSource;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-05-03 20:31
 * @created 1.0
 * @since 1.0
 */
public class GuiceContainerConfig extends PluggableContainerConfigBase {



    @Override
    public void register(InjectionRegistryBuilder registryBuilder) {

        activatePlugin(GuicePlugin.class).loadModules(new GuiceModule());

    }


}
