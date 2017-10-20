package org.hrodberaht.injection.extensions.cdi.example.service;

import javax.ejb.EJB;

/**
 * Created with IntelliJ IDEA.
 * User: alexbrob
 * Date: 2012-12-13
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class ExampleInterfaceImplementation implements ExampleInterface {

    @EJB
    private AnotherInterface anotherInterface;

    public String getSomething() {
        return "something";
    }

    public String getSomethingElseLikeWhat() {
        return anotherInterface.what();
    }
}
