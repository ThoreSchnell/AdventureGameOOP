package com.thehxlab.adventureengine.core;

import java.util.HashMap;

public class GameWorld {
	
	private HashMap<String, Room> rooms;
    private Player player;
    
    private String adventureName = "";
    private String adventureIntro = "";

    public GameWorld() {
        rooms = new HashMap<>();
        player = new Player();
    }

    public void addRoom(Room room) {
        rooms.put(room.getName(), room);
    }

    public Room getRoom(String name) {
        return rooms.get(name);
    }

    public void setPlayerLocation(String roomName) {
        Room room = rooms.get(roomName);
        if (room != null) {
            player.setCurrentRoom(room);
        }
    }

    public Room getPlayerLocation() {
        return player.getCurrentRoom();
    }

    public Player getPlayer() {
        return player;
    }
    
    public void setAdventureName(String name) {
    	this.adventureName = name;
    }
    
    public void setAdventureIntroduction(String intro) {
		this.adventureIntro = intro;
	}
    
    public String getAdventureName() {
        return adventureName;
    }

    public String getAdventureIntroduction() {
        return adventureIntro;
    }
}
