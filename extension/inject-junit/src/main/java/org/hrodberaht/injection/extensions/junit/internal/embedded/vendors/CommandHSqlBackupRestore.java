package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

import org.hrodberaht.injection.extensions.junit.internal.TDDLogger;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class CommandHSqlBackupRestore implements DatasourceBackupRestore{

    private final HsqlBDDataSourceConfigurationRestorable.HSQLDriverManager driverManager;
    private static final String lineSeparator = System.getProperty("line.separator");
    private static final String JDBC_BASEURL_NEW = "jdbc:hsqldb:file:";

    public CommandHSqlBackupRestore(HsqlBDDataSourceConfigurationRestorable.HSQLDriverManager driverManager) {
        this.driverManager = driverManager;
    }

    @Override
    public void createSnapshot(String name) {
        try (Connection connection = driverManager.getConnection()){

            JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceWrapper(connection));
            String backup = "BACKUP DATABASE TO "+name+" BLOCKING [ AS FILES ]";
            jdbcTemplate.execute(backup);
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void loadSnapshot(String name) {
        try (Connection connection = driverManager.getConnection();){

            JdbcTemplate jdbcTemplate = new JdbcTemplate(new DataSourceWrapper(connection));
            File file = new File(name);
            TDDLogger.log("---- BACKUPFILE CONTENT " + readFile(file));
            readFile(file, jdbcTemplate);
            connection.commit();
        } catch (IOException | SQLException e) {
            throw new RuntimeException(e);
        }
    }



    @Override
    public String jdbcUrl() {
        return JDBC_BASEURL_NEW;
    }

    private String readFile(File file) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);

        try {
            while (scanner.hasNextLine()) {
                fileContents.append(scanner.nextLine() + lineSeparator);
            }
            return fileContents.toString();
        } finally {
            scanner.close();
        }
    }

    private String readFile(File file, JdbcTemplate jdbcTemplate) throws IOException {
        StringBuilder fileContents = new StringBuilder((int) file.length());
        Scanner scanner = new Scanner(file);
        // String lineSeparator = System.getProperty("line.separator");

        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (shouldExecuteLine(line)) {
                    try{
                        jdbcTemplate.execute(line);
                    }catch (Exception e){
                        TDDLogger.log("Failed to restore line - "+line);
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
