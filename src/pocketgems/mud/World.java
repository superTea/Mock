package pocketgems.mud;

import java.util.HashMap;

import pocketgems.mud.components.IdentityComponent;
import pocketgems.mud.exceptions.EntityNotFoundException;

/*
 * World
 * =====
 * World contains the game state, which consists of a collection of entities. World also exposes
 * convenient and efficient methods to modify and retrieve the game state.
 */
public class World {
	private HashMap<String, Entity> entitiesById;
	private Entity player;

	public World(Entity player) {
		entitiesById = new HashMap<String, Entity>();
		this.player = player;
		AddEntity(player);
	}

	public void AddEntity(Entity entity) {
		IdentityComponent identityComponent = entity.getComponentOrNull(IdentityComponent.class);
		if (identityComponent != null) {
			entitiesById.put(identityComponent.id, entity);
		}
	}

	public Entity GetEntity(String id) throws EntityNotFoundException {
		Entity e = entitiesById.get(id);
		if (e == null) {
			throw new EntityNotFoundException(id);
		}
		return e;
	}

	public Entity GetPlayer() {
		return player;
	}
}
