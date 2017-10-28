package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

import org.slf4j.LoggerFactory;

import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class PlainConnection extends TestConnection{

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
