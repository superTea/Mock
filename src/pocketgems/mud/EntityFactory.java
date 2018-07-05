package pocketgems.mud;

import pocketgems.mud.components.DescriptionComponent;
import pocketgems.mud.components.IdentityComponent;
import pocketgems.mud.components.LocationComponent;
import pocketgems.mud.components.PortalComponent;
import pocketgems.mud.components.RoomComponent;
import pocketgems.mud.entity.*;
import pocketgems.mud.exceptions.ComponentNotFoundException;

public abstract class EntityFactory {

	public static Entity createEntity(String command) {
		Entity entity = null;
		if (command.equals("createroom")) {
			return new Room();
		} else if (command.equals("createexit")) {
			return new Exit();
		} else if (command.equals("creatething")) {
			return new Thing();
		} else if (command.equals("createitem")) {
			return new Item();
		}
		return entity;
	}
	
	// Based on the assumption that there is only one player in a world, we are hardcoding some of the parameters here for convenience.
	public static Player createPlayer() {
		Player player = new Player();
		try {
			player.getIdentityComponent().id = "player";
			player.getDescriptionComponent().name = "You";
			player.getDescriptionComponent().description = "You";
			return player;
		} catch (ComponentNotFoundException e) {
			return player;
		}
	}
}
