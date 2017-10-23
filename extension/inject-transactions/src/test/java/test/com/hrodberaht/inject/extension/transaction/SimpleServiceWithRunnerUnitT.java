package test.com.hrodberaht.inject.extension.transaction;

import com.hrodberaht.inject.extensions.transaction.junit.InjectionContainerContext;
import com.hrodberaht.inject.extensions.transaction.junit.InjectionJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import test.com.hrodberaht.inject.extension.transaction.example.ModuleContainerForSimpleTests;
import test.com.hrodberaht.inject.extension.transaction.example.SimpleServiceApplication;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-12 21:02:15
 * @version 1.0
 * @since 1.0
 */
@InjectionContainerContext(ModuleContainerForSimpleTests.class)
@RunWith(InjectionJUnitTestRunner.class)
public class SimpleServiceWithRunnerUnitT {

    @Inject
    private SimpleServiceApplication serviceApplication;

    @Test
    public void testSimpleService() {
        int value = serviceApplication.addOne(2);
        assertEquals(3, value);
    }

}
