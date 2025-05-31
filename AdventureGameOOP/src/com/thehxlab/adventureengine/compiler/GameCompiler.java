package com.thehxlab.adventureengine.compiler;

import java.io.IOException;

import com.thehxlab.adventureengine.core.GameWorld;
import com.thehxlab.adventureengine.core.Room;
import com.thehxlab.adventureengine.io.FileReaderUtil;

public class GameCompiler {

    public static GameWorld compile(String filePath) throws IOException {
        String[] lines = FileReaderUtil.readFile(filePath);
        GameWorld world = new GameWorld();

        world.setAdventureName(HSFLParser.parseAdventureName(lines));
        world.setAdventureIntroduction(HSFLParser.parseAdventureIntroduction(lines));

        // Parse rooms, connections and items
        Room[] rooms = HSFLParser.parseRooms(lines);
        HSFLParser.parseConnections(lines, rooms);
        HSFLParser.parseActors(lines, rooms);
        HSFLParser.parseItems(lines, rooms);

        // Add rooms to the world
        for (Room room : rooms) {
            if (room != null) {
                world.addRoom(room);
            }
        }

        // Set the start room
        String startRoomName = HSFLParser.parseStartRoom(lines);
        if (startRoomName != null) {
            world.setPlayerLocation(startRoomName);
        } else {
            throw new IllegalArgumentException("StartRoom not defined in .hsfl file.");
        }
        
        return world;
    }
}