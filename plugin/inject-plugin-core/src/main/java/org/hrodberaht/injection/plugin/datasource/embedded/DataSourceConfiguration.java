package org.hrodberaht.injection.plugin.datasource.embedded;


import org.hrodberaht.injection.plugin.datasource.DataSourceProxyInterface;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceConfiguration {
    Connection initateConnection() throws ClassNotFoundException, SQLException;

    void createSnapshot(String name);

    void loadSnapshot(String name);

    boolean runWithConnectionAndCommit(DataSourceProxyInterface.ConnectionRunner connectionRunner);
}
