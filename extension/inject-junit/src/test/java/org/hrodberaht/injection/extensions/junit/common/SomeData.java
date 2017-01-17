package org.hrodberaht.injection.extensions.junit.common;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by IntelliJ IDEA.
 * User: robban
 * Date: 2011-01-18
 * Time: 20:48
 * To change this template use File | Settings | File Templates.
 */
@Entity(name = "the_table")
public class SomeData {

    @Id
    private Long id;

    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
