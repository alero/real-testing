package test.service;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.sql.DataSource;

@javax.ejb.Stateless
public class CDIServiceInterfaceImpl implements CDIServiceInterface {

    @Resource(name = "MyDataSource")
    private DataSource dataSource;

    @Inject
    private SimpleServiceSingleton postContainer;

    @Inject
    private SimpleServiceSingletonAnnotated postContainerSingleton;

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

    @Override
    public SimpleServiceSingleton getLoadedPostContainer() {
        return postContainer;
    }


}
