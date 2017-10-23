package demo.service;


import javax.annotation.PostConstruct;
import javax.inject.Singleton;

@Singleton
public class Service {
    private String doSomething = "thing about it";

    @PostConstruct
    public void init() {
        doSomething = "done";
    }

    public String doIt() {
        return doSomething;
    }

}
