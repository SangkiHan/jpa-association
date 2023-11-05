package persistence.entity.attribute;

import fixtures.EntityFixtures;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class EntityAttributes {
    //TODO Entity Scan 구현
    private static final Map<Class<?>, EntityAttribute> entityAttributeCenter = new HashMap<>();

    static {
        entityAttributeCenter.put(EntityFixtures.Order.class, EntityAttribute.of(EntityFixtures.Order.class, createReferenceTracingLog()));
        entityAttributeCenter.put(EntityFixtures.OrderItem.class, EntityAttribute.of(EntityFixtures.OrderItem.class, createReferenceTracingLog()));
        entityAttributeCenter.put(EntityFixtures.EntityWithIntegerId.class, EntityAttribute.of(EntityFixtures.EntityWithIntegerId.class, createReferenceTracingLog()));
        entityAttributeCenter.put(EntityFixtures.EntityWithStringId.class, EntityAttribute.of(EntityFixtures.EntityWithStringId.class, createReferenceTracingLog()));
        entityAttributeCenter.put(EntityFixtures.SampleOneWithValidAnnotation.class, EntityAttribute.of(EntityFixtures.SampleOneWithValidAnnotation.class, createReferenceTracingLog()));
        entityAttributeCenter.put(EntityFixtures.SampleTwoWithValidAnnotation.class, EntityAttribute.of(EntityFixtures.SampleTwoWithValidAnnotation.class, createReferenceTracingLog()));
    }

    private static HashSet<Class<?>> createReferenceTracingLog() {
        return new HashSet<>();
    }

    public EntityAttribute findEntityAttribute(Class<?> clazz) {
        return entityAttributeCenter.get(clazz);
    }
}