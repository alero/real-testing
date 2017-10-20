package org.hrodberaht.injection.extensions.junit.internal;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-02-05 19:32
 * @created 1.0
 * @since 1.0
 */
public class DataSources {


    private Collection<DataSourceProxy> dataSources = new ArrayList<DataSourceProxy>();

    public DataSources(Collection<DataSourceProxy> dataSources) {
        this.dataSources = dataSources;
    }

    public Collection<DataSourceProxy> getDataSources() {
        return dataSources;
    }

    public void setDataSources(Collection<DataSourceProxy> dataSources) {
        this.dataSources = dataSources;
    }
}
