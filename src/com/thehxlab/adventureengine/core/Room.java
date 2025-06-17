package com.thehxlab.adventureengine.core;

public class Room {

    private String name;
    private String description;

    private Item[] items = new Item[10];
    private Room[] connections = new Room[4];
    private Lock[] locks = new Lock[4];
    private Actor[] actors = new Actor[5];

    public Room(String name, String desc) {
        this.name = name;
        this.description = desc;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Item[] getItems() {
        return items;
    }

    public void addItem(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == null) {
                items[i] = item;
                return;
            }
        }
        System.out.println("No space to add more items in the room.");
    }

    public void removeItem(Item item) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] == item) {
                items[i] = null;
                return;
            }
        }
    }

    public void addActor(Actor actor) {
        for (int i = 0; i < actors.length; i++) {
            if (actors[i] == null) {
                actors[i] = actor;
                return;
            }
        }
        System.out.println("The room is full and cannot hold more actors!");
    }

    public void removeActor(Actor actor) {
        for (int i = 0; i < actors.length; i++) {
            if (actors[i] == actor) {
                actors[i] = null;
                return;
            }
        }
    }

    public Actor[] getActors() {
        return actors;
    }

    public Actor getActorByName(String actorName) {
        for (Actor actor : actors) {
            if (actor != null && actor.getName().equalsIgnoreCase(actorName)) {
                return actor;
            }
        }
        return null;
    }

    public Room[] getConnections() {
        return connections;
    }

    public Lock[] getLocks() {
        return locks;
    }

    // Ursprüngliche Methode mit int
    public void connectRoom(Room room, int direction, Lock lock) {
        if (direction >= 0 && direction < connections.length) {
            connections[direction] = room;
            locks[direction] = lock;
        }
    }

    // NEU: Elegantere Methode mit Direction
    public void connectRoom(Room room, Direction direction, Lock lock) {
        connectRoom(room, direction.getIndex(), lock);
    }

    public boolean isAccessible(int direction) {
        return locks[direction] == null || locks[direction].isUnlocked();
    }

    // NEU: Methode mit Direction
    public boolean isAccessible(Direction direction) {
        return isAccessible(direction.getIndex());
    }

    public String getLockDescription(int direction) {
        if (locks[direction] != null) {
            return locks[direction].getDescription();
        }
        return null;
    }

    // NEU: Methode mit Direction
    public Room getConnectedRoom(Direction direction) {
        return connections[direction.getIndex()];
    }
}
