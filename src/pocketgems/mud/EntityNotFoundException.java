package pocketgems.mud;

/*
 * EntityNotFoundException
 * ======
 * Thrown when a user tries to access an entity that does not exist.  Contains the
 * identifier of the searched-for entity
 */
public class EntityNotFoundException extends Exception {
	private String entityId;

	public EntityNotFoundException(String entityId) {
		this.entityId = entityId;
	}

	public String getEntityId() {
		return entityId;
	}
}

