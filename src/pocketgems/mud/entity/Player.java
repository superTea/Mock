package pocketgems.mud.entity;

import pocketgems.mud.components.InventoryComponent;
import pocketgems.mud.components.LocationComponent;
import pocketgems.mud.exceptions.ComponentNotFoundException;
import pocketgems.mud.exceptions.EntityNotFoundException;

public class Player extends Entity {

    public Player() {
        super();
        addComponent(new LocationComponent());
        addComponent(new InventoryComponent());
    }

    public Room currentRoom()
            throws EntityNotFoundException,ComponentNotFoundException {
        return (Room) this.getLocationComponent().room();
    }

    @Override
    public void get() {
        System.out.println("You cannot take that");
    }

    @Override
    public void drop() {

    }

}
