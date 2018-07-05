package pocketgems.mud.components;

import pocketgems.mud.entity.Entity;
import pocketgems.mud.exceptions.EntityNotFoundException;
import pocketgems.mud.World;

/*
 * LocationComponent
 * =================
 * Put this component on entities that can exist in a room.
 */
public class LocationComponent extends Component {
	public String roomId;

	public Entity room() throws EntityNotFoundException {
		if (roomId == null) {
			return null;
		}
		return World.getInstance().getEntity(roomId);
	}
}