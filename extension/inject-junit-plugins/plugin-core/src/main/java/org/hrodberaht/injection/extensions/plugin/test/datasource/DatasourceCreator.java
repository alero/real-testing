package org.hrodberaht.injection.extensions.plugin.test.datasource;

import javax.sql.DataSource;

public interface DatasourceCreator {

    DataSource createDataSource(String dataSourceName);


}
