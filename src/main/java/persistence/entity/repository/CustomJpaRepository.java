package persistence.entity.repository;

import persistence.entity.attribute.EntityAttribute;
import persistence.entity.manager.EntityManager;

import java.lang.reflect.Field;
import java.util.HashSet;

public class CustomJpaRepository<T, ID> {
    private final EntityManager entityManager;

    public CustomJpaRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public T save(T instance) {
        EntityAttribute entityAttribute = EntityAttribute.of(instance.getClass(),new HashSet<>());
        Field idField = entityAttribute.getIdAttribute().getField();
        idField.setAccessible(true);
        return entityManager.persist(instance);
    }
}