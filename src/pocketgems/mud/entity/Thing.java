package pocketgems.mud.entity;

import pocketgems.mud.components.LocationComponent;
import pocketgems.mud.exceptions.ComponentNotFoundException;
import pocketgems.mud.exceptions.EntityNotFoundException;

public class Thing extends Entity {

    public Thing () {
        super();
        addComponent(new LocationComponent());
    }

    @Override
    public void get() throws ComponentNotFoundException, EntityNotFoundException {
        System.out.println("You cannot take that");
    }

    @Override
    public void drop() throws ComponentNotFoundException, EntityNotFoundException {

    }
}
