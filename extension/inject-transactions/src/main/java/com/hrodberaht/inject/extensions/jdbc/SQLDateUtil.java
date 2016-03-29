package com.hrodberaht.inject.extensions.jdbc;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Inject Transactions
 *
 * @author Robert Alexandersson
 *         2010-sep-16 21:33:10
 * @version 1.0
 * @since 1.0
 */
public class SQLDateUtil {


    public static java.util.Date getDate(java.sql.Date date){
        Date aDate = new Date();
        aDate.setTime(date.getTime());
        return aDate;
    }

    public static java.util.Date getDate(java.sql.Timestamp date){
        Date aDate = new Date();
        aDate.setTime(date.getTime());
        return aDate;
    }

    public static Timestamp getCalendar(java.util.Calendar date){
        return new Timestamp(date.getTime().getTime());
    }

     public static java.util.Calendar getCalendar(Timestamp date){
        Calendar aDate = Calendar.getInstance();
        aDate.setTime( new Date(date.getTime()) );        
        return aDate;
    }

}
