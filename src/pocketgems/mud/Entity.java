package pocketgems.mud;

import java.util.HashMap;

import pocketgems.mud.components.Component;

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
	
	public <T extends Component> T getComponent(Class<T> componentType) {
		return (T)components.get(componentType);
	}
}
