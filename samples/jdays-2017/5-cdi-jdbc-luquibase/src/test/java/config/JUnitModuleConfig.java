package config;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerCreator;
import com.hrodberaht.inject.extensions.transaction.junit.TransactionManagedTesting;
import com.hrodberaht.inject.extensions.transaction.manager.JdbcModule;
import liquibase.exception.LiquibaseException;
import org.hrodberaht.injection.InjectContainer;
import org.hrodberaht.injection.core.internal.InjectionRegisterModule;
import org.hrodberaht.injection.extensions.junit.datasource.FileTimestampResourceWatcher;
import org.hrodberaht.injection.extensions.junit.datasource.liquibase.LiquibaseUtil;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class JUnitModuleConfig extends TDDCDIContainerConfigBase implements InjectionContainerCreator, TransactionManagedTesting {

    private String dataStoreDir = "target/liquibase/";
    private String liquieBaseSchema = "db.changelog-test.xml";

    protected static void createDirectoryIfNotExists(File dir) {
        if (!dir.exists()) {
            try {
                Files.createDirectories(dir.toPath());
            } catch (final IOException e) {
                throw new IllegalStateException("Failed to create test directories " + dir.toPath(), e);
            }
        }
    }

    public InjectContainer createContainer() {
        // TransactionLogging.enableLogging = true;

        InjectionRegisterModule register = new InjectionRegisterModule();


        String name = "jdbc/MyDatasource";
        DataSource dataSource = createDataSource(name);
        addResource(name, dataSource);

        createLiquibaseSchema(dataSource, "db.changelog-test.xml");

        super.addSQLSchemas(name, "sql");

        register.register(new JdbcModule(dataSource));
        return register.getContainer();
    }

    private void createLiquibaseSchema(DataSource dataSource, String... watchers) {
        try {
            createDirectoryIfNotExists(new File(dataStoreDir));
            FileTimestampResourceWatcher fileTimestampResourceWatcher = new FileTimestampResourceWatcher(
                    new File(dataStoreDir, "filewatcher.log"), asResources(watchers)
            );

            new LiquibaseUtil(
                    "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'SIMPLE'"
                    , getLiquibaseBackupFile().getPath(), fileTimestampResourceWatcher
            )
                    .liquiBaseSchemaCreation(dataSource, liquieBaseSchema);
        } catch (SQLException | LiquibaseException e) {
            throw new RuntimeException(e);
        }
    }

    public File getLiquibaseBackupFile() {
        return new File(dataStoreDir, "backup.script");
    }


    private String[] asResources(String[] schemas) {
        List<String> strings = new ArrayList<>(schemas.length);
        Stream.of(schemas).forEach(s -> strings.add("classpath:" + s));
        return strings.toArray(new String[schemas.length]);
    }


}