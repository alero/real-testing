package demo.entity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Injection Transaction Extension
 *
 * @author Robert Alexandersson
 * 2010-aug-10 18:32:03
 * @version 1.0
 * @since 1.0
 */
@Entity
public class AnEntity {

    @Id
    private String name;
    private String value;

    // For JPA
    public AnEntity() {
    }

    public AnEntity(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
