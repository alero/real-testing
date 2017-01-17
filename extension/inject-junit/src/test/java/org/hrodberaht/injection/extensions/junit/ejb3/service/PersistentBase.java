package org.hrodberaht.injection.extensions.junit.ejb3.service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

/**
 * Created by IntelliJ IDEA.
 * User: robban
 * Date: 2011-01-18
 * Time: 20:50
 * To change this template use File | Settings | File Templates.
 */
public class PersistentBase {


    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;

    @Resource
    protected DataSource typedDataSource;

}
