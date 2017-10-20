package org.hrodberaht.injection.extensions.junit.internal;

import org.hrodberaht.injection.spi.ContainerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import java.sql.SQLException;

/**
 * Inject extension TDD
 *
 * @author Robert Alexandersson
 * 2011-02-05 19:26
 * @created 1.0
 * @since 1.0
 */
public class TransactionManager {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionManager.class);

    private static final ThreadLocal<EntityManagers> MANAGERS = new ThreadLocal<EntityManagers>();
    private static final ThreadLocal<DataSources> DATA_SOURCES = new ThreadLocal<DataSources>();

    public static void endTransaction() {
        EntityManagers entityManagers = MANAGERS.get();
        if (entityManagers != null) {
            for (EntityManager entityManager : entityManagers.getEntityManagers()) {
                if (entityManager != null) {

                    entityManager.getTransaction().rollback();
                    entityManager.clear();
                    // entityManager.close();
                    LOG.debug("entityManager rollback " + entityManager);
                }
            }
        }

        DataSources dataSources = DATA_SOURCES.get();
        if (dataSources != null) {
            for (DataSourceProxy dataSourceProxy : dataSources.getDataSources()) {
                dataSourceProxy.clearDataSource();
                LOG.debug("dataSourceProxy rollback " + dataSourceProxy);
            }
        }
    }

    public static void beginTransaction(ContainerConfig creator) {

        MANAGERS.set(new EntityManagers(creator.getResourceCreator().getEntityManagers()));
        DATA_SOURCES.set(new DataSources(creator.getResourceCreator().getDataSources()));

        EntityManagers entityManagers = MANAGERS.get();
        if (entityManagers != null) {
            for (EntityManager entityManager : entityManagers.getEntityManagers()) {
                if (entityManager != null) {

                    entityManager.getTransaction().begin();
                    // entityManager.close();
                    LOG.debug("entityManager begin " + entityManager);
                }
            }
        }
        DataSources dataSources = DATA_SOURCES.get();
        if (dataSources != null) {
            for (DataSourceProxy dataSourceProxy : dataSources.getDataSources()) {
                try {
                    dataSourceProxy.getConnection();
                } catch (SQLException e) {
                }
                LOG.debug("dataSourceProxy Begin " + dataSourceProxy);
            }
        }
    }

}
