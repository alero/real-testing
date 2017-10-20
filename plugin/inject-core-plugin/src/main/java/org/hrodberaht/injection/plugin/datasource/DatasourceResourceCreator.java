package org.hrodberaht.injection.plugin.datasource;

import org.hrodberaht.injection.plugin.junit.datasource.DatasourceCreator;
import org.hrodberaht.injection.spi.JavaResourceCreator;

import javax.sql.DataSource;

public class DatasourceResourceCreator implements JavaResourceCreator<DataSource> {

    private final DatasourceCreator datasourceCreator;

    public DatasourceResourceCreator(DatasourceCreator datasourceCreator) {
        this.datasourceCreator = datasourceCreator;
    }

    public Class getType() {
        return DataSource.class;
    }

    @Override
    public DataSource create(String dataSourceName) {
        return datasourceCreator.createDataSource(dataSourceName);
    }

    @Override
    public DataSource create() {
        return datasourceCreator.createDataSource("defaultDataSource");
    }

    @Override
    public DataSource create(String name, DataSource instance) {
        return instance;
    }

    @Override
    public DataSource create(DataSource instance) {
        return instance;
    }
}
