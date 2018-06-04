package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class PlainConnection extends TestConnection {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(PlainConnection.class);


    public PlainConnection(Connection connection) {
        super(connection);
        LOG.info("Creating connection {}", this);
    }

    @Override
    public void commit() throws SQLException {
        LOG.info("commit {}", this);
        super.commitIt();
    }

    @Override
    public void close() throws SQLException {
        // super.closeIt();
    }
}
