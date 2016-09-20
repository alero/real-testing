package org.hrodberaht.injection.extensions.junit.internal.embedded;

import org.hrodberaht.injection.spi.DataSourceProxyInterface;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceConfiguration {
    Connection initateConnection() throws ClassNotFoundException, SQLException;

    void createSnapshot(String name);

    void loadSnapshot(String name);

    void runWithConnectionAndCommit(DataSourceProxyInterface.ConnectionRunner connectionRunner);
}
