package org.hrodberaht.injection.extensions.junit.internal.embedded.vendors;

public interface DatasourceBackupRestore {
    void createSnapshot(String name);
    void loadSnapshot(String name);

    String jdbcUrl();
}
