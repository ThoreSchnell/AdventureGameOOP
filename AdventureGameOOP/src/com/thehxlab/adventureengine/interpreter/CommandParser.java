package com.thehxlab.adventureengine.interpreter;

public class CommandParser {

    public static String[] parse(String input) {
        if (input == null || input.trim().isEmpty()) {
            return new String[0]; // Empty input
        }
        return input.trim().toLowerCase().split("\\s+", 2); // Split into command and arguments
    }
}
