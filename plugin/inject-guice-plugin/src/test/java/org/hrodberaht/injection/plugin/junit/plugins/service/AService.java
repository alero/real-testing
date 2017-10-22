package org.hrodberaht.injection.plugin.junit.plugins.service;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class AService implements AnInterface {

    String something = "empty";

    @Inject
    private MoreServices moreServices;

    @PostConstruct
    public void init() {
        something = "inited";
    }

    @Override
    public String doSomething() {
        return something;
    }

    @Override
    public MoreServices getService() {
        return moreServices;
    }
}
