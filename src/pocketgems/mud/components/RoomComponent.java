package pocketgems.mud.components;

import java.util.HashSet;

/*
 * RoomComponent
 * =============
 * Room entities should have a RoomComponent, which allows them to contain inhabitants and exits to
 * other rooms.
 */
public class RoomComponent extends Component {
	public HashSet<String> inhabitantIds;
	public HashSet<String> exitIds;
	
	public RoomComponent() {
		inhabitantIds = new HashSet<String>();
		exitIds = new HashSet<String>();
	}
}
