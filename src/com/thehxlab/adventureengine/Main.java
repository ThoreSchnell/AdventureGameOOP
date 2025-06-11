package com.thehxlab.adventureengine;

import java.io.IOException;

import com.thehxlab.adventureengine.GUIs.Overlays;
import com.thehxlab.adventureengine.compiler.GameCompiler;
import com.thehxlab.adventureengine.core.GameWorld;
import com.thehxlab.adventureengine.interpreter.GameInterpreter;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {

        try {
            // Compile the game world from the .hsfl file
            String filePath = "adventures/forrest.hsfl";
            GameWorld world = GameCompiler.compile(filePath);
            Overlays frame = new Overlays(world);
            frame.setVisible(true);
            // Start the game interpreter
            GameInterpreter interpreter = new GameInterpreter(world);
            interpreter.start();

        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Error parsing the file: " + e.getMessage());
        }


    }
}