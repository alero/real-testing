package test.com.hrodberaht.inject.extension.transaction.util;

import test.com.hrodberaht.inject.extension.transaction.example.Logging;
import test.com.hrodberaht.inject.extension.transaction.example.Person;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-11 22:58:13
 * @version 1.0
 * @since 1.0
 */
public class StubUtil {
    private static AtomicLong id = new AtomicLong(1L);

    public static long getNextId(){
         return id.incrementAndGet();
    }

    public static Person createPerson() {
        Person person = new Person();
        person.setId(getNextId());
        person.setName("Dude");
        return person;
    }

    public static Logging createLogg(String message){
        Logging logg = new Logging();
        logg.setId(getNextId());
        logg.setMessage(message);
        return logg;
    }
}
