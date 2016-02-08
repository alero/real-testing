package test.org.hrodberaht.inject.extension.cdi.cdi_ext;

import test.org.hrodberaht.inject.extension.cdi.service.ConstantClassLoadedPostContainer;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterBeanDiscovery;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.ProcessAnnotatedType;
import javax.inject.Inject;


/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 12:25
 * To change this template use File | Settings | File Templates.
 */
public class CDIExtension implements Extension {

    @Inject
    public ConstantClassLoadedPostContainer constantClassLoadedPostContainer;

    void beforeBeanDiscovery(@Observes BeforeBeanDiscovery bbd) {

        System.out.println("beginning the scanning process");

    }


    <T> void processAnnotatedType(@Observes ProcessAnnotatedType<T> pat) {

        System.out.println("scanning type: " + pat.getAnnotatedType().getJavaClass().getName());

    }


    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd) {

        System.out.println("finished the scanning process");
        constantClassLoadedPostContainer.init();

    }


}
