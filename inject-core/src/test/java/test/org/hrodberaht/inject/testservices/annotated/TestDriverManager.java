package test.org.hrodberaht.inject.testservices.annotated;

import org.hrodberaht.injection.scope.InheritableThreadScope;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-jun-06 03:20:10
 * @version 1.0
 * @since 1.0
 */
@InheritableThreadScope
public class TestDriverManager {

    @Inject
    Provider<Car> carProvider;

    @Inject
    @Spare
    Provider<Tire> tireProvider;

    private String name = null;
    private String initTextSpecial = null;

    public Car getCar() {
        return carProvider.get();
    }

    public Tire getTire() {
        return tireProvider.get();
    }

    @PostConstructInit
    public void initSpecial() {
        initTextSpecial = "Initialized special";
    }

    public String getInitTextSpecial() {
        return initTextSpecial;
    }
}
