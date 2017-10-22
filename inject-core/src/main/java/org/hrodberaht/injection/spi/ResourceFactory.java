package org.hrodberaht.injection.spi;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-11-28
 * Time: 13:55
 * To change this template use File | Settings | File Templates.
 */
public interface ResourceFactory {

    <T> JavaResourceCreator<T> getCreator(Class<T> type);

    <T> void addResourceCrator(JavaResourceCreator<T> tJavaResourceCreator);
}
