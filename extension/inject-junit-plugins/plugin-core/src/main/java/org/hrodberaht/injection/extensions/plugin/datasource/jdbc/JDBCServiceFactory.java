package org.hrodberaht.injection.extensions.plugin.datasource.jdbc;

import org.hrodberaht.injection.extensions.plugin.datasource.jdbc.internal.InsertOrUpdaterImpl;
import org.hrodberaht.injection.extensions.plugin.datasource.jdbc.internal.JDBCException;
import org.hrodberaht.injection.extensions.plugin.datasource.jdbc.internal.JDBCServiceImpl;
import org.hrodberaht.injection.internal.exception.InjectRuntimeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

/**
 * @author Robert Alexandersson
 */
public class JDBCServiceFactory {

    public static JDBCService of(DataSource dataSource){
        return new JDBCServiceImpl(new InsertOrUpdaterImpl(), dataSource);
    }

}
