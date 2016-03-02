package org.hrodberaht.inject.internal.annotation.creator;

import org.hrodberaht.inject.internal.stats.Statistics;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Simple Java Utils - Container
 *
 * @author Robert Alexandersson
 *         2010-jun-06 00:18:20
 * @version 1.0
 * @since 1.0
 */
public class InstanceCreatorDefault implements InstanceCreator {

    public Object createInstance(Constructor constructor, Object... parameters) {
        try {
            Statistics.addNewInstanceCount();
            return constructor.newInstance(parameters);

        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e){
            throw new RuntimeException(e);
        }
    }
}
