package pocketgems.mud.entity;

import pocketgems.mud.components.PortalComponent;
import pocketgems.mud.exceptions.ComponentNotFoundException;
import pocketgems.mud.exceptions.EntityNotFoundException;

public class Exit extends Entity {

    public Exit() {
        super();
        addComponent(new PortalComponent());
    }

    @Override
    public void get() {
        System.out.println("You cannot take that");
    }

    @Override
    public void drop() {

    }
}
