package org.hrodberaht.inject.extension.cdi.example.service;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2014-06-26
 * Time: 09:24
 * To change this template use File | Settings | File Templates.
 */
public class CDIExampleExtension implements Extension {

    private static boolean afterBeanDiscoveryInitiated = false;
    private static boolean beforeBeanDiscoveryInitiated = false;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {
        beforeBeanDiscoveryInitiated = true;
    }


    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {
    }


    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {
        afterBeanDiscoveryInitiated = true;
    }

    public boolean isAfterBeanDiscoveryInitiated() {
        return afterBeanDiscoveryInitiated;
    }

    public boolean isBeforeBeanDiscoveryInitiated() {
        return beforeBeanDiscoveryInitiated;
    }
}
