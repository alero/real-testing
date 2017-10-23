/*
 * Copyright (c) 2017 org.hrodberaht
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.hrodberaht.injection.plugin.datasource.embedded.vendors;

import java.io.PrintWriter;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

class DataSourceWrapper implements javax.sql.DataSource {

    private final Connection connection;
    private final Connection proxy;

    public DataSourceWrapper(Connection connection) {
        this.connection = connection;
        proxy = createNoCloseProxy();
    }

    private Connection createNoCloseProxy() {
        InvocationHandler invocationHandler = (proxy, method, args) -> {
            if (method.getName().equals("close")) {
                // do nothing
                return null;
            }
            return method.invoke(connection, args);
        };
        return (Connection) Proxy.newProxyInstance(
                Thread.currentThread().getContextClassLoader(), new Class[]{Connection.class}, invocationHandler
        );
    }

    @Override
    public Connection getConnection() throws SQLException {
        return proxy;
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return proxy;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        return null;
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return false;
    }

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) throws SQLException {

    }

    @Override
    public void setLoginTimeout(int seconds) throws SQLException {

    }

    @Override
    public int getLoginTimeout() throws SQLException {
        return 0;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }
}

