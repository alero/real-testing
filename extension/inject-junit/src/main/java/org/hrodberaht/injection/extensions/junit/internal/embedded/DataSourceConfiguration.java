package org.hrodberaht.injection.extensions.junit.internal.embedded;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataSourceConfiguration {
    Connection initateConnection() throws ClassNotFoundException, SQLException;

    void createSnapshot();

    void loadSnapshot();
}
