package demo.service;


import demo.entity.AnEntity;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class Service {

    @PersistenceContext(unitName = "example-jpa")
    protected EntityManager entityManager;

    public void createIt(String key, String value) {
        entityManager.persist(new AnEntity(key, value));
    }

    public void changeIt(String key, String value) {
        AnEntity theTable = entityManager.find(AnEntity.class, key);
        theTable.setValue(value);
    }

    public String findIt(String key) {
        return entityManager.find(AnEntity.class, key).getValue();

    }

}
