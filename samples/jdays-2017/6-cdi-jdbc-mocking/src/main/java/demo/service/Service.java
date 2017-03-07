package demo.service;


import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Service {
    @Inject
    private ServiceBean wrapper;

    public String doIt(){
        return wrapper.doIt();
    }

}
