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

package org.hrodberaht.injection.extensions.plugin.junit.datasource.service;

import org.hrodberaht.injection.extensions.plugin.junit.model.User;

import javax.sql.DataSource;

public interface UserService {

    User find(String userName);

    void changePassword(String userName, String newPassword);

    boolean loginCompare(String userName, String password);

    DataSource getTypedDataSource();

    DataSource getNamedDataSource();

    void createUser(String userName, String password);
}
