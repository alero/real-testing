package test.com.hrodberaht.inject.extension.transaction.example;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerCreator;
import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagedTesting;
import com.hrodberaht.inject.extensions.transaction.manager.JdbcModule;
import org.hibernate.internal.SessionFactoryImpl;
import org.hrodberaht.injection.core.InjectContainer;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class ModuleContainerForJDBCHelperTests implements InjectionContainerCreator, TransactionManagedTesting {

    public static InjectContainer container;

    public ModuleContainerForJDBCHelperTests() {
    }

    public static DataSource getDataSource(final EntityManagerFactory entityManagerFactory) {

        DataSource simpleDataSource = new DataSource() {
            PrintWriter printWriter = null;

            public Connection getConnection() throws SQLException {
                return ((SessionFactoryImpl) entityManagerFactory)
                        .getJdbcServices()
                        .getBootstrapJdbcConnectionAccess()
                        .obtainConnection();
            }

            public Connection getConnection(String username, String password) throws SQLException {
                return getConnection();
            }

            public PrintWriter getLogWriter() throws SQLException {
                return printWriter;
            }

            public void setLogWriter(PrintWriter out) throws SQLException {
                printWriter = out;
            }

            public int getLoginTimeout() throws SQLException {
                return 600;
            }

            public void setLoginTimeout(int seconds) throws SQLException {

            }

            public Logger getParentLogger() throws SQLFeatureNotSupportedException {
                return null;
            }

            public <T> T unwrap(Class<T> iface) throws SQLException {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            public boolean isWrapperFor(Class<?> iface) throws SQLException {
                return false;
            }
        };
        return simpleDataSource;


    }

    public InjectContainer createContainer() {
        InjectionRegisterModule register = new InjectionRegisterModule();

        // This is just done to simplify the test application
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-jpa");


        register.register(JDBCHelperApplication.class);
        // Create the JdbcModule for a data-source
        DataSource dataSource = getDataSource(entityManagerFactory);
        register.register(new JdbcModule(dataSource));

        InjectContainer injectContainer = register.getContainer();
        container = injectContainer;
        return injectContainer;
    }
}