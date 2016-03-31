package org.hrodberaht.injection.extensions.cdi.example.service;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
@ApplicationScoped
public class AnotherInterfaceImplementation implements AnotherInterface {

    @Resource( name = DataSourceNames.SAMPLE)
    DataSource dataSource;

    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;

    public String what() {
        return "wait for it";
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
