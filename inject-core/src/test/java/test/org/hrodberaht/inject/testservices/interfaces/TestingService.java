package test.org.hrodberaht.inject.testservices.interfaces;

import javax.inject.Inject;

/**
 * Created by alexbrob on 2016-03-01.
 */
public class TestingService implements TestingServiceInterface {

    @Inject
    private TestingServiceInnerInterface testingServiceInnerInterface;

}
