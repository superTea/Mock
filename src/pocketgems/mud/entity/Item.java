package pocketgems.mud.entity;

import pocketgems.mud.World;
import pocketgems.mud.components.IdentityComponent;
import pocketgems.mud.components.LocationComponent;
import pocketgems.mud.components.RoomComponent;
import pocketgems.mud.exceptions.ComponentNotFoundException;
import pocketgems.mud.exceptions.EntityNotFoundException;

public class Item extends Thing {

    @Override
    public void get()
            throws ComponentNotFoundException, EntityNotFoundException {
        removeFromRoom();
        moveToPlayerInventory();
        System.out.println("You acquire " + this.getDescriptionComponent().description + '.');
    }

    protected void removeFromRoom()
            throws ComponentNotFoundException, EntityNotFoundException {
        IdentityComponent identityComponent = this.getIdentityComponent();
        LocationComponent locationComponent = this.getLocationComponent();

        Entity room = locationComponent.room();
        if (room != null) {
            room.getRoomComponent().inhabitantIds.remove(identityComponent.id);
        }
        locationComponent.roomId = null;
    }

    protected void moveToPlayerInventory()
            throws ComponentNotFoundException {
        World.getInstance().getPlayer().getInventoryComponent().addItem(this);
    }

    @Override
    public void drop()
            throws ComponentNotFoundException, EntityNotFoundException {
        removeFromPlayerInventory();
        String currentRoom = World.getInstance().getPlayer().getLocationComponent().room().getIdentityComponent().id;
        moveToRoom(currentRoom);
        System.out.println("You drop " + this.getDescriptionComponent().description + '.');
    }

    protected void removeFromPlayerInventory()
            throws EntityNotFoundException, ComponentNotFoundException {
        World.getInstance().getPlayer().getInventoryComponent().dropItem(this);
    }


    protected void moveToRoom(String destinationRoomId)
            throws ComponentNotFoundException, EntityNotFoundException {
        IdentityComponent identityComponent = this.getIdentityComponent();
        LocationComponent locationComponent = this.getLocationComponent();

        Entity room = locationComponent.room();
        if (room != null) {
            room.getRoomComponent().inhabitantIds.remove(identityComponent.id);
        }

        locationComponent.roomId = destinationRoomId;

        RoomComponent destinationRoomComponent = World.getInstance().getEntity(destinationRoomId).getRoomComponent();
        destinationRoomComponent.inhabitantIds.add(identityComponent.id);
    }
}
