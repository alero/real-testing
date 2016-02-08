package org.hrodberaht.inject.extension.cdi.cdiext;

import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeforeBeanDiscovery;
import java.lang.annotation.Annotation;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 14:06
 * To change this template use File | Settings | File Templates.
 */
public class BeforeBeanDiscoveryByInject implements BeforeBeanDiscovery {


    public void addQualifier(Class<? extends Annotation> aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addScope(Class<? extends Annotation> aClass, boolean b, boolean b1) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addStereotype(Class<? extends Annotation> aClass, Annotation... annotations) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addInterceptorBinding(Class<? extends Annotation> aClass) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void addAnnotatedType(AnnotatedType<?> annotatedType) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
