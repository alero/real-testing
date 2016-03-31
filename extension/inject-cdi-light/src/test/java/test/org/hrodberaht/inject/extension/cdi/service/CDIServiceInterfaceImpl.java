package test.org.hrodberaht.inject.extension.cdi.service;

import javax.ejb.Stateless;
import javax.inject.Inject;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-03
 * Time: 09:35
 * To change this template use File | Settings | File Templates.
 */
@Stateless
public class CDIServiceInterfaceImpl implements CDIServiceInterface {

    @Inject
    private ConstantClassLoadedPostContainer postContainer;

    public String findSomething(long l) {
        return "Something";
    }

    public String findSomethingDeep(long l) {
        return postContainer.getInitData();
    }
}
