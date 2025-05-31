package com.thehxlab.adventureengine.core;

public class Player {
	
	private Room currentRoom;
	
    //private ArrayList<Item> inventory; laters ;)
	private Item[] inventory = new Item[5]; // Assume 5 items max

    public Player() {
       //this.inventory = new ArrayList<>();
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }

    public Item[] getInventory() {
        return inventory;
    }

    public void addItemToInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                return;
            }
        }
        System.out.println("Your inventory is full!");
    }

    public void removeItemFromInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == item) {
                inventory[i] = null; // Remove the item
                return;
            }
        }
        System.out.println("That item is not in your inventory.");
    }


    @Override
    public String toString() {
        return "Player is in: " + (currentRoom != null ? currentRoom.getName() : "nowhere") +
                ". Inventory: " + inventory.toString();
    }
}
