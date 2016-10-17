package test.service;

import javax.inject.Inject;
import javax.inject.Singleton;

@javax.ejb.Stateless
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
