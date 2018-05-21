package pocketgems.mud.components;

/*
 * IdentityComponent
 * =================
 * Put this component on any entity that may be referenced by input. In our identification system,
 * the ids are provided by the inputs that create the entities, so that future inputs can reference
 * those entities by id. Each entity should have a unique id. An id is not meant to be user facing.
 * Generally, world building commands will reference entities by id, and game play commands will
 * reference entities by "keyword", which is defined in the DescriptionComponent.
 */

public class IdentityComponent extends Component {
	
	public String id;
}
