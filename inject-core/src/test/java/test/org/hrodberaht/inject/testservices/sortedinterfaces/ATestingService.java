package test.org.hrodberaht.inject.testservices.sortedinterfaces;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by alexbrob on 2016-03-01.
 */
@Singleton
public class ATestingService implements ATestingServiceInterface {

    @Inject
    private BTestingServiceInnerInterface BTestingServiceInnerInterface;

}
