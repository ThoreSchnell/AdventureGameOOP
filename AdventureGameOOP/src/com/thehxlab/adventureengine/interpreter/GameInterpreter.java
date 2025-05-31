package com.thehxlab.adventureengine.interpreter;

import java.util.Scanner;

import com.thehxlab.adventureengine.core.GameWorld;

public class GameInterpreter {
	
	private final GameWorld world;
    private final CommandParser parser;
    private final CommandExecutor executor;

    public GameInterpreter(GameWorld world) {
        this.world = world;
        this.parser = new CommandParser();
        this.executor = new CommandExecutor(world);
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.println(world.getAdventureName().toUpperCase());
        System.out.println();
        System.out.println(wrapText(world.getAdventureIntroduction(), 40));
        System.out.println();
        System.out.println("========================================");
        System.out.println("         Available Commands             ");
        System.out.println("========================================");
        System.out.println("look       : Inspect your surroundings.");
        System.out.println("inventory  : Check your items.");
        System.out.println("use        : Use an item");
        System.out.println("combine    : Combine items");
        System.out.println("talk       : Talk to someone");
        System.out.println("go         : Move to another room");
        System.out.println("take       : Take an item");
        System.out.println("quit       : Exit the game");
        System.out.println("========================================");

        while(true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            System.out.println(processCommand(input));
        }
    }
    
    public String processCommand(String input) {
        String[] parsedInput = parser.parse(input);
        if (parsedInput.length == 0) {
            return "Bitte gib einen Befehl ein.";
        }

        String command = parsedInput[0];
        String argument = parsedInput.length > 1 ? parsedInput[1] : null;
        return executor.execute(command, argument);
    }
    
    public static String wrapText(String text, int maxWidth) {
        StringBuilder wrappedText = new StringBuilder();
        int index = 0;
        while (index < text.length()) {
            int nextBreak = Math.min(index + maxWidth, text.length());
            if (nextBreak < text.length() && text.charAt(nextBreak) != ' ') {
                nextBreak = text.lastIndexOf(' ', nextBreak);
                if (nextBreak <= index) {
                    nextBreak = index + maxWidth; // Force break if no space is found
                }
            }
            wrappedText.append(text, index, nextBreak).append("\n");
            index = nextBreak + 1; // Move past the space
        }
        return wrappedText.toString().trim();
    }
}
