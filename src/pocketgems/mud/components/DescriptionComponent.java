package pocketgems.mud.components;

import java.util.HashSet;

public class DescriptionComponent extends Component {
	public String name;
	public String description;
	
	/* Keywords are strings that can be used to reference the entity in an input. For example,
	 * to go north one may enter "go north" or "go n". In this case, "north" and "n" are keywords
	 * for the entity representing the exit towards the room in that direction.
	 */
	public HashSet<String> keywords;
	
	public DescriptionComponent() {
		keywords = new HashSet<String>();
	}
}
