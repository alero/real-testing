package test.org.hrodberaht.inject.extension.cdi.service;

import javax.annotation.PostConstruct;
import javax.inject.Singleton;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 09:36
 * To change this template use File | Settings | File Templates.
 */
@Singleton
public class ConstantClassLoadedPostContainer {

    private String initData = null;

    @PostConstruct
    public void init() {
        initData = "initialized";
    }

    public String getInitData() {
        return initData;
    }
}
