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

package org.hrodberaht.injection.plugin.datasource.jdbc;

import java.util.List;

public interface JDBCService {

    InsertOrUpdater createInsertOrUpdate(String table);

    Insert createInsert(String table);

    int insertOrUpdate(InsertOrUpdater insertOrUpdater);

    int insert(Insert insert);

    <T> List<T> query(String sql, RowIterator<T> rowIterator, Object... args);

    <T> T querySingle(String sql, RowIterator<T> rowIterator, Object... args);


    int execute(String sql, Object... args);
}
