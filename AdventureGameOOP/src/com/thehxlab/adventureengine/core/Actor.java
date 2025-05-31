package com.thehxlab.adventureengine.core;

public class Actor {
	
	private String name;
    private String description;
    private Room currentRoom;
    
    private String quest;
    private String reward;
    
    private Item[] inventory = new Item[5]; // Fixed-size inventory for simplicity

    public Actor(String name, String description) {
        this.name = name;
        this.description = description;
        this.quest = null;
        this.reward = null;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
    
    public void setCurrentRoom(Room r) {
    	this.currentRoom = r;
    }
    
    public Room getCurrentRoom() {
    	return this.currentRoom;
    }
    
    public void addItemToInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                inventory[i] = item;
                return;
            }
        }
        System.out.println(name + "'s inventory is full!");
    }

    public void removeItemFromInventory(Item item) {
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == item) {
                inventory[i] = null;
                return;
            }
        }
    }

    public Item[] getInventory() {
        return inventory;
    }
    
    public String talk() {
    	
    	if (this.quest != null) {
			return name + " says: " + this.quest;
		}
        return name + " says: 'Hello! Can I help you with something?'";
    }
    
    public void moveTo(Room newRoom) {
        this.currentRoom = newRoom;
        System.out.println(name + " moves to " + newRoom.getName());
    }
    
    public String getQuest() {
    	return this.quest;
    }
    
    public void setQuest(String text, String reward) {
    	this.quest = text;
    	this.reward = reward;
    }
    
    public String giveReward(Player p) {
    	if (quest == null) {
			return "You have nothing more to ask.";
		}
    	
    	p.addItemToInventory(new Item(reward, "A special reward for completing the quest of the "  + name));
    	quest = null;
    	return "Thank you for completing the task. Here is your reward: " + reward;
    }

}
