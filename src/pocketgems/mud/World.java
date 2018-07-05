package pocketgems.mud;

import java.util.HashMap;

import pocketgems.mud.components.IdentityComponent;
import pocketgems.mud.entity.Entity;
import pocketgems.mud.entity.Player;
import pocketgems.mud.exceptions.EntityNotFoundException;

/*
 * World
 * =====
 * World contains the game state, which consists of a collection of entities. World also exposes
 * convenient and efficient methods to modify and retrieve the game state.
 */
public class World {
	private HashMap<String, Entity> entitiesById;
	private Player player;
	private static World instance;

	public World() {
		entitiesById = new HashMap<String, Entity>();
		player = EntityFactory.createPlayer();
		addEntity(player);
	}

	public static synchronized World getInstance() {
		if (instance == null) {
			instance = new World();
		}
		return instance;
	}

	public void addEntity(Entity entity) {
		IdentityComponent identityComponent = entity.getComponentOrNull(IdentityComponent.class);
		if (identityComponent != null) {
			entitiesById.put(identityComponent.id, entity);
		}
	}

	public Entity getEntity(String id) throws EntityNotFoundException {
		Entity e = entitiesById.get(id);
		if (e == null) {
			throw new EntityNotFoundException(id);
		}
		return e;
	}

	public Player getPlayer() {
		return player;
	}
}
