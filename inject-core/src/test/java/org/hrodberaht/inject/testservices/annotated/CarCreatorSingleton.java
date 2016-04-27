package org.hrodberaht.inject.testservices.annotated;

import javax.inject.Singleton;
import java.util.Date;

/**
 * Qmatic Booking Module
 *
 * @author Robert Alexandersson
 *         2011-04-04 22:18
 * @created 1.0
 * @since 1.0
 */
@Singleton
public class CarCreatorSingleton {

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
