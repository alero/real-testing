package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

import java.sql.SQLException;

public interface VendorDriverManager {
    TestConnection getConnection() throws SQLException;
}
