package org.hrodberaht.injection.plugin.junit.datasource;

import javax.sql.DataSource;

public interface DatasourceCreator {

    DataSource createDataSource(String dataSourceName);


}