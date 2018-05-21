package pocketgems.mud;

import pocketgems.mud.components.DescriptionComponent;
import pocketgems.mud.components.IdentityComponent;
import pocketgems.mud.components.LocationComponent;
import pocketgems.mud.components.PortalComponent;
import pocketgems.mud.components.RoomComponent;

public abstract class EntityFactory {
	public static Entity createRoom() {
		Entity entity = new Entity();
		entity.addComponent(new IdentityComponent());
		entity.addComponent(new DescriptionComponent());
		entity.addComponent(new RoomComponent());
		return entity;
	}
	
	public static Entity createThing() {
		Entity entity = new Entity();
		entity.addComponent(new IdentityComponent());
		entity.addComponent(new DescriptionComponent());
		entity.addComponent(new LocationComponent());
		return entity;
	}

	public static Entity createExit() {
		Entity entity = new Entity();
		entity.addComponent(new IdentityComponent());
		entity.addComponent(new DescriptionComponent());
		entity.addComponent(new PortalComponent());
		return entity;
	}
	
	// Based on the assumption that there is only one player in a world, we are hardcoding some of the parameters here for convenience.
	public static Entity createPlayer() {
		Entity entity = new Entity();
		
		IdentityComponent identityComponent = new IdentityComponent();
		identityComponent.id = "player";
		entity.addComponent(identityComponent);

		DescriptionComponent descriptionComponent = new DescriptionComponent();
		descriptionComponent.name = "You";
		descriptionComponent.description = "You";
		entity.addComponent(descriptionComponent);
		
		entity.addComponent(new LocationComponent());
		
		return entity;
	}
}
