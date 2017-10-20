package org.hrodberaht.injection.plugin.junit.plugins.service.config;

import com.google.inject.AbstractModule;
import org.hrodberaht.injection.plugin.junit.plugins.service.AService;
import org.hrodberaht.injection.plugin.junit.plugins.service.AnInterface;
import org.hrodberaht.injection.plugin.junit.plugins.service.MoreServices;

public class GuiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(AnInterface.class).to(AService.class).asEagerSingleton();
        bind(MoreServices.class).asEagerSingleton();
    }
}
