package test.service;

import javax.annotation.PostConstruct;

public class SimpleService {

    private String initData = null;

    @PostConstruct
    public void init() {
        initData = "initialized";
    }

    public String getInitData() {
        return initData;
    }
}
