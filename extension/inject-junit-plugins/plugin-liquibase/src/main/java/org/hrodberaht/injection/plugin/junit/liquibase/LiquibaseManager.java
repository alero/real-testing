package org.hrodberaht.injection.plugin.junit.liquibase;

import liquibase.exception.LiquibaseException;
import org.hrodberaht.injection.plugin.junit.ResourceWatcher;
import org.hrodberaht.injection.plugin.junit.file.FileTimestampResourceWatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.io.File;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class LiquibaseManager {

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseManager.class);
    private final String verificationQuery;
    private final File dataStoreDir;

    public LiquibaseManager(String name, String verificationQuery) {
        this.verificationQuery = verificationQuery;
        dataStoreDir = getBackUpDirectory(name);
        if(!dataStoreDir.exists()){
            dataStoreDir.mkdirs();
        }
    }

    public void liquiBaseSchemaCreation(DataSource dataSource, String schema, String... watchers) throws SQLException, LiquibaseException {
        File watcherFile = getLiquibaseFile("filewatcher.log");
        LOG.info("using watcher file {}", watcherFile.getAbsolutePath());
        FileTimestampResourceWatcher fileTimestampResourceWatcher = new FileTimestampResourceWatcher(
                watcherFile , asResources(watchers, schema)
        );
        liquiBaseSchemaCreation(dataSource, schema, fileTimestampResourceWatcher);
    }

    private File getBackUpDirectory(String name) {
        if(System.getProperty("hrodberaht.test.liquibase.home") != null){
            return new File(System.getProperty("hrodberaht.test.liquibase.home")+name);
        }
        return new File("target/liquibase/"+name);
    }

    private File getLiquibaseBackupFile(){
        return new File(dataStoreDir, "backup.script");
    }

    private File getLiquibaseFile(String fileName){
        return new File(dataStoreDir, fileName);
    }

    private String[] asResources(String[] schemas, String schema) {
        if(schemas.length > 0) {
            List<String> strings = new ArrayList<>(schemas.length);
            Stream.of(schemas).forEach(s -> {
                String resource = "classpath:" + s;
                strings.add(resource);
                LOG.info("watching resource '{}'", resource);
            });
            return strings.toArray(new String[schemas.length]);
        }else{
            String resource = "classpath:" + schema;
            LOG.info("watching resource '{}'", resource);
            return new String[]{resource};
        }
    }

    private void liquiBaseSchemaCreation(
            DataSource dataSource, String liquieBaseSchema,
            ResourceWatcher resourceWatcher) throws SQLException, LiquibaseException {

        File backupFile = getLiquibaseBackupFile();
        LOG.info("using backup file {}", backupFile.getAbsolutePath());

        new LiquibaseRunner(
                verificationQuery
                , backupFile.getPath(), resourceWatcher
        ).liquiBaseSchemaCreation(dataSource, liquieBaseSchema);
    }
}