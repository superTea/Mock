package pocketgems.mud.exceptions;

import pocketgems.mud.entity.Entity;
import pocketgems.mud.components.Component;

/*
 * ComponentNotFoundException
 * ======
 * Thrown when an expected component is not found on an Entity
 */
public class ComponentNotFoundException extends Exception {
    private Entity entity;
    private Class componentType;

    public <T extends Component> ComponentNotFoundException(Entity entity, Class<T> componentType) {
        this.entity = entity;
        this.componentType = componentType;
    }

    public Entity getEntity() {
        return entity;
    }

    public Class getComponentType() {
        return componentType;
    }
}
