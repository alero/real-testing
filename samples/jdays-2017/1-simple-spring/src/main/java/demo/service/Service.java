package demo.service;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
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
