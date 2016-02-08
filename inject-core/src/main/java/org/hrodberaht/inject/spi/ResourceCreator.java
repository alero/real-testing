package org.hrodberaht.inject.spi;

import javax.sql.DataSource;
import java.util.Collection;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
public interface ResourceCreator<T, K extends DataSource> {
    K createDataSource(String dataSourceName);

    boolean hasDataSource(String dataSourceName);

    K getDataSource(String schemaName);

    T createEntityManager(String schemaName, String dataSourceName, DataSource dataSource);

    Collection<T> getEntityManagers();

    Collection<K> getDataSources();
}
