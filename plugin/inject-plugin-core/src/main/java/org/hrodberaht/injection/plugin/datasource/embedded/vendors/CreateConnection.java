package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

public interface CreateConnection<T extends TestConnection> {
    T create();
}
