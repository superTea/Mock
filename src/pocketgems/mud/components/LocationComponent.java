package pocketgems.mud.components;

import pocketgems.mud.Entity;
import pocketgems.mud.World;

/*
 * LocationComponent
 * =================
 * Put this component on entities that can exist in a room.
 */
public class LocationComponent extends Component {
	public String roomId;

	public Entity room(World world) {
		return world.GetEntity(roomId);
	}
}