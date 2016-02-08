package test.org.hrodberaht.inject.testservices.annotated;

import org.hrodberaht.inject.register.InjectionFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Robert Work
 * Date: 2010-aug-18
 * Time: 15:31:20
 * To change this template use File | Settings | File Templates.
 */
public class VolvoFactory implements InjectionFactory<Volvo> {
    public Volvo getInstance() {
        Volvo volvo = new Volvo();
        volvo.setInformation("Made from factory");
        return volvo;
    }

    public Class getInstanceType() {
        return Volvo.class;
    }

    public boolean newObjectOnInstance() {
        return true;
    }
}
