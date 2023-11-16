package persistence.entity;

import java.util.Objects;

public class EntityKey {
	private final Class<?> clazz;

	private final Object id;

	public EntityKey(Class<?> clazz, Object id) {
		this.clazz = clazz;
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null || getClass() != obj.getClass()) {
			return false;
		}

		EntityKey entityKey = (EntityKey) obj;

		return hashCode() == entityKey.hashCode();
	}

	@Override
	public int hashCode() {
		return Objects.hash(clazz, id);
	}
}