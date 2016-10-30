package test.service;


import javax.sql.DataSource;

public interface CDIServiceInterface {


    String findSomething(long l);

    String findSomethingDeep(long l);

    DataSource getDataSource();
}
