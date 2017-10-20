package org.hrodberaht.injection.extensions.cdi.example.service;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public interface AnotherInterface {

    String what();

    DataSource getDataSource();

    EntityManager getEntityManager();

}
