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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

public class SQLDateUtil {

    public static Date getDate(java.sql.Date date) {
        Date aDate = new Date();
        aDate.setTime(date.getTime());
        return aDate;
    }

    public static Date getDate(Timestamp date) {
        Date aDate = new Date();
        aDate.setTime(date.getTime());
        return aDate;
    }

    public static Timestamp getCalendar(Calendar date) {
        return new Timestamp(date.getTime().getTime());
    }

    public static Calendar getCalendar(Timestamp date) {
        Calendar aDate = Calendar.getInstance();
        aDate.setTime(new Date(date.getTime()));
        return aDate;
    }

}
