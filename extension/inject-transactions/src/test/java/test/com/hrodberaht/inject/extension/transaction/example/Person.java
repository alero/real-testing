package test.com.hrodberaht.inject.extension.transaction.example;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 *         2010-aug-10 18:32:03
 * @version 1.0
 * @since 1.0
 */
@Entity
public class Person {

    @Id
    private Long id;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
