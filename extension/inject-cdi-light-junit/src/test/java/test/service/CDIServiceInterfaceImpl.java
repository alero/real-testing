package test.service;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.sql.DataSource;

@javax.ejb.Stateless
public class CDIServiceInterfaceImpl implements CDIServiceInterface {

    @Resource(name = "MyDataSource")
    private DataSource dataSource;

    @Inject
    private ConstantClassLoadedPostContainer postContainer;

    public String findSomething(long l) {
        return "Something";
    }

    public String findSomethingDeep(long l) {
        return postContainer.getInitData();
    }

    @Override
    public DataSource getDataSource() {
        return dataSource;
    }
}
