package org.hrodberaht.injection.plugin.datasource.jdbc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * @author Robert Alexandersson
 */
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
