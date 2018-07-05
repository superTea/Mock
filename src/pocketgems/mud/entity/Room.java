package pocketgems.mud.entity;

import pocketgems.mud.components.RoomComponent;

public class Room extends Entity {

    public Room() {
        super();
        addComponent(new RoomComponent());
    }

    @Override
    public void get() {
        System.out.println("You cannot take that");
    }

    @Override
    public void drop() {

    }

}
