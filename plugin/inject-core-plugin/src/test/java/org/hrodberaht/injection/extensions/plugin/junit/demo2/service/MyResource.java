package org.hrodberaht.injection.extensions.plugin.junit.demo2.service;

public class MyResource {

    private final String value;

    public MyResource(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }
}
