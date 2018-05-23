package pocketgems.mud;

import java.util.HashMap;

import pocketgems.mud.components.*;
import pocketgems.mud.exceptions.ComponentNotFoundException;

/*
 * Entity
 * ======
 * An entity is a set of components, with no more than one instance of any component type. An entire
 * game state should consist of nothing more than a set of Entities.
 * 
 */
public class Entity {
	private HashMap<Class, Component> components;
	
	public Entity() {
		components = new HashMap<Class, Component>();
	}
	
	public <T extends Component> T addComponent(T component) {
		components.put(component.getClass(), component);
		return component;
	}
	
	public <T extends Component> T getComponent(Class<T> componentType) throws ComponentNotFoundException {
		T component = (T)components.get(componentType);
		if (component == null) {
			throw new ComponentNotFoundException(this, componentType);
		}
		return component;
	}

	public <T extends Component> T getComponentOrNull(Class<T> componentType) {
		try {
			return getComponent(componentType);
		} catch (ComponentNotFoundException exception) {
			return null;
		}
	}

	// Convenience accessors
	public DescriptionComponent getDescriptionComponent() throws ComponentNotFoundException { return getComponent(DescriptionComponent.class); }
	public IdentityComponent getIdentityComponent() throws ComponentNotFoundException { return getComponent(IdentityComponent.class); }
	public LocationComponent getLocationComponent() throws ComponentNotFoundException { return getComponent(LocationComponent.class); }
	public PortalComponent getPortalComponent() throws ComponentNotFoundException { return getComponent(PortalComponent.class); }
	public RoomComponent getRoomComponent() throws ComponentNotFoundException { return getComponent(RoomComponent.class); }
}
