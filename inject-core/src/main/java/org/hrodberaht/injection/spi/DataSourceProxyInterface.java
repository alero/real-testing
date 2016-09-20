package org.hrodberaht.injection.spi;

import java.sql.Connection;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2014-06-04
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
public interface DataSourceProxyInterface extends javax.sql.DataSource {

    void clearDataSource();

    void commitDataSource();

    Connection getNativeConnection();

    void loadSnapshot(String name);

    void createSnapshot(String name);

    boolean runWithConnectionAndCommit(ConnectionRunner connectionRunner) throws Exception;

    interface ConnectionRunner{
        boolean run(Connection con) throws Exception;
    }

}
