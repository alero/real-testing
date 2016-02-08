package test.org.hrodberaht.inject.testservices.annotated;

/**
 * Simple Java Utils
 *
 * @author Robert Alexandersson
 *         2010-aug-02 22:29:42
 * @version 1.0
 * @since 1.0
 */
public abstract class BaseTire {
    private String brand;

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }
}
