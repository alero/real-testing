package demo.service;


import javax.annotation.PostConstruct;

public class ServiceBean {
    private String doSomething = "thing about it";

    @PostConstruct
    public void init() {
        doSomething = "done";
    }

    public String doIt() {
        return doSomething;
    }

}
