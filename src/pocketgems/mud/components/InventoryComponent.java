package pocketgems.mud.components;

import pocketgems.mud.World;
import pocketgems.mud.entity.Item;
import pocketgems.mud.exceptions.ComponentNotFoundException;
import pocketgems.mud.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InventoryComponent extends Component {
    private HashMap<String, Integer> inventories;

    public InventoryComponent() {
        inventories = new HashMap<>();
    }

    public void addItem(Item item)
            throws ComponentNotFoundException {
        String itemID = item.getIdentityComponent().id;
        inventories.put(itemID, inventories.getOrDefault(itemID, 1));
    }

    public void dropItem(Item item) throws
            EntityNotFoundException, ComponentNotFoundException {
        String itemID = item.getIdentityComponent().id;
        if (inventories.containsKey(itemID)) {
            int amount = inventories.get(itemID);
            if (amount == 1) {
                inventories.remove(itemID);
            } else {
                inventories.put(itemID, amount - 1);
            }
        } else {
            throw new EntityNotFoundException(itemID);
        }
    }

    public String inventoryList(World world)
            throws EntityNotFoundException, ComponentNotFoundException {
        StringBuilder str = new StringBuilder();
        if (inventories.size() == 0) {
            str.append("There are no items in your inventory");
        } else {
            str.append("Your inventory:\n");
            for (String itemID : inventories.keySet()) {
                Item item = (Item) world.getEntity(itemID);
                str.append("\t- " + item.getDescriptionComponent().description + "\n");
            }
        }

        return str.toString();
    }

    public List<Item> allInventories()
            throws EntityNotFoundException {
        List<Item> list = new ArrayList<>();
        for (String itemID : inventories.keySet()) {
            Item item = (Item) World.getInstance().getEntity(itemID);
            list.add(item);
        }
        return list;
    }
}
