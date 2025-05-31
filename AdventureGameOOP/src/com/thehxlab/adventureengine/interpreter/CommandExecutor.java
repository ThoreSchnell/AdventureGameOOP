package com.thehxlab.adventureengine.interpreter;

import com.thehxlab.adventureengine.compiler.HSFLParser;
import com.thehxlab.adventureengine.core.Actor;
import com.thehxlab.adventureengine.core.GameWorld;
import com.thehxlab.adventureengine.core.Item;
import com.thehxlab.adventureengine.core.Lock;
import com.thehxlab.adventureengine.core.Player;
import com.thehxlab.adventureengine.core.Room;

public class CommandExecutor {

    private final GameWorld world;

    public CommandExecutor(GameWorld world) {
        this.world = world;
    }

    public String execute(String command, String argument) {
        Player player = world.getPlayer();
        Room currentRoom = player.getCurrentRoom();
        String result = "";

        switch (command) {
        
	        case "look":
	            result += "You are in: " + currentRoom.getName() + "\n";
	            result += currentRoom.getDescription() + "\n";
	
	            // Display items in the room
	            boolean hasItems = false;
	            for (Item item : currentRoom.getItems()) {
	                if (item != null) {
	                	result += "You see a " + item.getName() + ": " + item.getDescription() + "\n";
	                    hasItems = true;
	                }
	            }
	            if (!hasItems) {
	            	result += "There are no items here." + "\n";
	            }
	
	            // Display available exits
	            result += "Exits:" + "\n";
	            Room[] connections = currentRoom.getConnections();
	            Lock[] locks = currentRoom.getLocks();
	            for (int i = 0; i < connections.length; i++) {
	                if (connections[i] != null) {
	                    String direction = HSFLParser.getDirectionName(i);
	                    if (locks[i] == null || locks[i].isUnlocked()) {
	                    	result += "- " + direction + " to the " + connections[i].getName() + "\n";
	                    } else {
	                    	result += "- " + direction + " (blocked: " + locks[i].getDescription() + ")" + "\n";
	                    }
	                }
	            }
	            
	            // Display actors in the room
	            Actor[] actors = currentRoom.getActors();
	            boolean hasActors = false;
	            for (Actor actor : actors) {
	                if (actor != null) {
	                	result += "You see " + actor.getName() + ": " + actor.getDescription() + "\n";
	                    hasActors = true;
	                }
	            }
	            if (!hasActors) {
	            	result += "There is no one here." + "\n";
	            }
	            
	            break;


            case "take":
                if (argument == null) {
                	result += "Take what?" + "\n";
                } else {
                    boolean found = false;
                    for (Item item : currentRoom.getItems()) {
                        if (item != null && item.getName().equalsIgnoreCase(argument)) {
                            player.addItemToInventory(item);
                            currentRoom.removeItem(item);
                            result += "You take the " + argument + "." + "\n";
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                    	result += "There is no " + argument + " here." + "\n";
                    }
                }
                break;

            case "inventory":
            	result += "You are carrying:" + "\n";
                boolean empty = true;
                for (Item item : player.getInventory()) {
                    if (item != null) {
                    	result += "- " + item.getName() + ": " + item.getDescription() + "\n";
                        empty = false;
                    }
                }
                if (empty) {
                	result += "Nothing." + "\n";
                }
                break;
            
            case "use":
                if (argument == null) {
                	result += "Use what with what?" + "\n";
                } else {
                    String[] parts = argument.split(" with ");
                    if (parts.length == 2) {
                        String itemName = parts[0].trim();
                        String target = parts[1].trim();

                        // Check if target is a direction (for locks)
                        try {
                            int directionIndex = HSFLParser.getDirectionIndex(target);
                            Lock lock = currentRoom.getLocks()[directionIndex];

                            if (lock != null && !lock.isUnlocked()) {
                                // Check if the player has the required item
                                String requiredItem = lock.getRequiredItem();
                                boolean hasItem = false;
                                for (Item inventoryItem : player.getInventory()) {
                                	
                                    if (inventoryItem != null && inventoryItem.getName().equalsIgnoreCase(requiredItem)) {
                                        hasItem = true;
                                        break;
                                    }
                                }

                                if (hasItem) {
                                    lock.unlock(requiredItem);
                                    result += "You unlock the " + lock.getDescription() + " with the " + itemName + "." + "\n";
                                } else {
                                	result += "You don't have the correct item to unlock the " + lock.getDescription() + "." + "\n";
                                }
                            } else {
                            	result += "There is nothing to unlock " + target + "." + "\n";
                            }
                        } catch (IllegalArgumentException e) {
                            // If target is not a direction, fallback to item usage logic
                            useItemWith(itemName, target, player, currentRoom);
                        }
                    } else {
                    	result += "Usage: use <item1> with <item2> or use <item> with <direction>" + "\n";
                    }
                }
                break;

            
            case "combine":
                if (argument == null) {
                	result += "Combine what with what?" + "\n";
                } else {
                    String[] parts = argument.split(" with ");
                    if (parts.length == 2) {
                        String item1Name = parts[0].trim();
                        String item2Name = parts[1].trim();
                        combineItems(item1Name, item2Name, player);
                    } else {
                    	result += "Usage: combine <item1> with <item2>" + "\n";
                    }
                }
                break;
                
            case "go":
                if (argument == null) {
                	result += "Go where?" + "\n";
                } else {
                    try {
                        int directionIndex = HSFLParser.getDirectionIndex(argument);
                        Lock lock = currentRoom.getLocks()[directionIndex];
                        Room nextRoom = currentRoom.getConnections()[directionIndex];

                        if (nextRoom == null) {
                        	result += "You can't go " + argument + " from here." + "\n";
                        } else if (lock == null || lock.isUnlocked()) {
                            player.setCurrentRoom(nextRoom);
                            result += "You go " + argument + " to the " + nextRoom.getName() + "." + "\n";
                            result += nextRoom.getDescription() + "\n";
                        } else {
                        	result += "The way " + argument + " is blocked: " + lock.getDescription() + "\n";
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage());
                    }
                }
                break;
            
            case "talk":
                if (argument == null) {
                	result += "Talk to whom?" + "\n";
                } else {
                    String actorName = argument.trim();
                    Actor actor = currentRoom.getActorByName(actorName);
                    
                    if (actor != null) {
                    	result += actor.talk() + "\n";
                    } else {
                    	result += "There is no one named " + actorName + " here." + "\n";
                    }
                }
                break;


            case "quit":
            	result += "Thanks for playing!" + "\n";
                System.exit(0);
                break;

            default:
            	result += "I don't understand that command." + "\n";
                break;
        }
		return result;
    }
    
    private void useItemWith(String item1Name, String item2Name, Player player, Room currentRoom) {
        Item item1 = findItem(item1Name, player, currentRoom);
        Item item2 = findItem(item2Name, player, currentRoom);

        if (item1 == null || item2 == null) {
            System.out.println("You can't use " + item1Name + " with " + item2Name + ".");
            return;
        }

        if (item1.getUseRules() != null) {
            for (String rule : item1.getUseRules()) {
                if (rule.equalsIgnoreCase(item2.getName())) {
                    System.out.println("You successfully use " + item1Name + " with " + item2Name + ".");
                    return; // Success, perform additional logic if needed
                }
            }
        }

        System.out.println("You can't use " + item1Name + " with " + item2Name + ".");
    }
    
    private void combineItems(String item1Name, String item2Name, Player player) {
        Item item1 = findItem(item1Name, player, null);
        Item item2 = findItem(item2Name, player, null);

        if (item1 == null || item2 == null) {
            System.out.println("You can't combine " + item1Name + " with " + item2Name + ".");
            return;
        }

        if (item1.getCombineRules() != null) {
            for (String rule : item1.getCombineRules()) {
                String[] parts = rule.split("=");
                if (parts.length == 2 && parts[0].trim().equalsIgnoreCase(item2.getName())) {
                    String resultName = parts[1].trim();
                    player.removeItemFromInventory(item1);
                    player.removeItemFromInventory(item2);
                    player.addItemToInventory(new Item(resultName, "A combined item: " + resultName));
                    System.out.println("You combine " + item1Name + " with " + item2Name + " to create " + resultName + "!");
                    return;
                }
            }
        }

        System.out.println("You can't combine " + item1Name + " with " + item2Name + ".");
    }

    private Item findItem(String itemName, Player player, Room currentRoom) {
        // Check the player's inventory first
        for (Item item : player.getInventory()) {
            if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                return item;
            }
        }

        // If currentRoom is null, skip searching the room
        if (currentRoom != null) {
            for (Item item : currentRoom.getItems()) {
                if (item != null && item.getName().equalsIgnoreCase(itemName)) {
                    return item;
                }
            }
        }

        return null; // Item not found
    }
}
