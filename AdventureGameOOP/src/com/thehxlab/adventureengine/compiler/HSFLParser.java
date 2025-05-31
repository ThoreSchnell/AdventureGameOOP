package com.thehxlab.adventureengine.compiler;

import com.thehxlab.adventureengine.core.Actor;
import com.thehxlab.adventureengine.core.Item;
import com.thehxlab.adventureengine.core.Lock;
import com.thehxlab.adventureengine.core.Room;

public class HSFLParser {

	public static Room[] parseRooms(String[] lines) {
        Room[] rooms = new Room[10]; // Fixed array size for simplicity
        int roomIndex = 0;
        
        for (int i = 0; i < lines.length; i++) {
           
        	String line = lines[i].trim();
            
            if (line.startsWith("Room:")) {
                String name = line.substring(5).trim();
                String description = extractNextLine(lines, i, "Description:");
                rooms[roomIndex++] = new Room(name, description);
            }
        }
        return rooms;
    }
	
	public static void parseConnections(String[] lines, Room[] rooms) {
	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i].trim();
	        
	        if (line.startsWith("Room:")) {
	            String roomName = line.substring(5).trim();
	            Room currentRoom = findRoomByName(roomName, rooms);

	            String connectionsLine = extractOptionalLine(lines, i, "Connections:");
	            if (connectionsLine != null) {
	                String[] connections = connectionsLine.split(",");
	                for (String connection : connections) {
	                	
	                    String[] parts = connection.split("=", 2);
	                    
	                    if (parts.length >= 2) {
	                        String targetRoomName = parts[0].trim();
	                        String directionAndLock = parts[1].trim();
	                        
	                        
	                        String[] directionParts = directionAndLock.split("\\(");
	                        String direction = directionParts[0].trim();
	                        Lock lock = null;
	                        
	                        // Parse lock details
	                        if (directionParts.length > 1) {
	                            String lockDetails = directionParts[1].replace(")", "").trim();
	                            
	                            if (lockDetails.startsWith("locked=")) {
	                                String requiredItemName = lockDetails.substring(7).trim();
	                                lock = new Lock("Locked door", requiredItemName);
	                            }
	                        }

	                        Room targetRoom = findRoomByName(targetRoomName, rooms);
	                        if (targetRoom != null) {
	                            int directionIndex = getDirectionIndex(direction);
	                            currentRoom.connectRoom(targetRoom, directionIndex, lock);
	                        }
	                    }
	                }
	            }
	        }
	    }
	}
	
	public static Actor[] parseActors(String[] lines, Room[] rooms) {
	    Actor[] actors = new Actor[10]; // Fixed-size array for simplicity
	    int actorIndex = 0;
	    for (int i = 0; i < lines.length; i++) {
	        String line = lines[i].trim();
	        if (line.startsWith("Actor:")) {
	            String name = line.substring(6).trim();
	            String description = extractNextLine(lines, i, "Description:");
	            String locationName = extractNextLine(lines, i, "Location:");

	            Actor actor = new Actor(name, description);
	            actors[actorIndex++] = actor;

	            // Assign actor to room
	            for (Room room : rooms) {
	                if (room != null && room.getName().equalsIgnoreCase(locationName)) {
	                    room.addActor(actor);
	                    break;
	                }
	            }
	        }
	    }
	    return actors;
	}


	public static Item[] parseItems(String[] lines, Room[] rooms) {
        Item[] items = new Item[20]; // Fixed array size for simplicity
        int itemIndex = 0;
        for (int i = 0; i < lines.length; i++) {
            String line = lines[i].trim();
            if (line.startsWith("Item:")) {
                String name = line.substring(5).trim();
                String description = extractNextLine(lines, i, "Description:");
                String locationName = extractNextLine(lines, i, "Location:");
                String useRules = extractOptionalLine(lines, i, "Use:");
                String combineRules = extractOptionalLine(lines, i, "Combine:");

                Item item = new Item(name, description);
                if (useRules != null) {
                    item.setUseRules(useRules.split(","));
                }
                if (combineRules != null) {
                    item.setCombineRules(combineRules.split(","));
                }
                items[itemIndex++] = item;

                // Assign item to room
                for (Room room : rooms) {
                    if (room != null && room.getName().equalsIgnoreCase(locationName)) {
                        room.addItem(item);
                        break;
                    }
                }
            }
        }
        return items;
    }

    public static String parseStartRoom(String[] lines) {
        for (String line : lines) {
            if (line.startsWith("StartRoom:")) {
                return line.substring(11).trim();
            }
        }
        return null;
    }

    public static String parseAdventureName(String[] lines) {
        for (String line : lines) {
            if (line.startsWith("AdventureName:")) {
                return line.substring(14).trim();
            }
        }
        return "Unnamed Adventure"; // Default name
    }

    public static String parseAdventureIntroduction(String[] lines) {
        for (String line : lines) {
            if (line.startsWith("Introduction:")) {
                return line.substring(13).trim();
            }
        }
        return "Welcome to the adventure!"; // Default introduction
    }
    
    private static Room findRoomByName(String name, Room[] rooms) {
	    for (Room room : rooms) {
	        if (room != null && room.getName().equalsIgnoreCase(name)) {
	            return room;
	        }
	    }
	    return null;
	}

	public static int getDirectionIndex(String direction) {
	    switch (direction.toLowerCase()) {
	        case "north":
	            return 0;
	        case "east":
	            return 1;
	        case "south":
	            return 2;
	        case "west":
	            return 3;
	        default:
	            throw new IllegalArgumentException("Invalid direction: " + direction);
	    }
	}
	
	public static String getDirectionName(int index) {
	    switch (index) {
	        case 0:
	            return "north";
	        case 1:
	            return "east";
	        case 2:
	            return "south";
	        case 3:
	            return "west";
	        default:
	            throw new IllegalArgumentException("Invalid direction index: " + index);
	    }
	}

    private static String extractOptionalLine(String[] lines, int currentIndex, String prefix) {
        for (int i = currentIndex + 1; i < lines.length; i++) {
            String nextLine = lines[i].trim();
            if (nextLine.startsWith(prefix)) {
                return nextLine.substring(prefix.length()).trim();
            }
        }
        return null;
    }

    private static String extractNextLine(String[] lines, int currentIndex, String prefix) {
        for (int i = currentIndex + 1; i < lines.length; i++) {
            String nextLine = lines[i].trim();
            if (nextLine.startsWith(prefix)) {
                return nextLine.substring(prefix.length()).trim();
            }
        }
        throw new IllegalArgumentException("Expected " + prefix + " after line " + currentIndex);
    }
}