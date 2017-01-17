package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

import org.hrodberaht.injection.config.ClassScanner;
import org.hrodberaht.injection.extensions.junit.internal.TDDLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class PlainHSqlBackupRestore implements DatasourceBackupRestore{


    private static final Logger LOG = LoggerFactory.getLogger(PlainHSqlBackupRestore.class);

    private final HsqlBDDataSourceConfigurationRestorable.HSQLDriverManager driverManager;
    private static final String JDBC_BASEURL_NEW = "jdbc:hsqldb:mem:";

    public PlainHSqlBackupRestore(HsqlBDDataSourceConfigurationRestorable.HSQLDriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Override
    public void createSnapshot(final String name) {

        final String fileName = getFilename(name);

        LOG.info("PlainHSqlBackupRestore backup to : {}", fileName);


        try (final Connection connection = driverManager.getConnection()){

            JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceWrapper(connection));
            String backup = "SCRIPT '" + fileName + "'";
            jdbcTemplate.execute(backup);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private String getFilename(String name) {
        String customBackup = System.getProperty("hrodberaht.junit.sql.backupFile");
        if(customBackup != null) {
            name = customBackup;
        }
        return name;
    }

    @Override
    public void loadSnapshot(String name) {

        final String fileName = getFilename(name);
        LOG.debug("PlainHSqlBackupRestore restore from : " + fileName);

        try (Connection connection = driverManager.getConnection();){

            JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceWrapper(connection));
            readFile(new File(fileName), jdbcTemplate);
            connection.commit();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String jdbcUrl() {
        return JDBC_BASEURL_NEW;
    }

    private String readFile(File file, JdbcTemplate jdbcTemplate) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);

        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (shouldExecuteLine(line)) {
                    try{
                        jdbcTemplate.execute(line);
                    }catch (Exception e){
                        LOG.debug("Failed to restore line - "+line);
                    }
                }
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    private boolean shouldExecuteLine(String line) {
        return !
                ("CREATE SCHEMA PUBLIC AUTHORIZATION DBA".equals(line)
                        || line.contains("CREATE USER SA PASSWORD DIGEST")
                        || line.contains("GRANT DBA TO SA")
                        || line.equals("SET SCHEMA SYSTEM_LOBS")
                        || line.contains("INSERT INTO BLOCKS VALUES")
                );
    }
}
