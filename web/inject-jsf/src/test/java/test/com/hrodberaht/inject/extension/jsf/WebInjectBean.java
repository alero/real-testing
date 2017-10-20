package test.com.hrodberaht.inject.extension.jsf;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

/**
 * Injection Extension Web
 *
 * @author Robert Alexandersson
 * 2010-jul-28 22:52:12
 * @version 1.0
 * @since 1.0
 */
@ManagedBean(name = "personBean")
@RequestScoped
public class WebInjectBean {

    @Inject
    private ServiceInject serviceInject;

    public ServiceInject getServiceInject() {
        return serviceInject;
    }

    public void setServiceInject(ServiceInject serviceInject) {
        this.serviceInject = serviceInject;
    }
}
