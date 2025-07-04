package com.thehxlab.adventureengine;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import com.thehxlab.adventureengine.GUIs.Overlays;
import com.thehxlab.adventureengine.compiler.GameCompiler;
import com.thehxlab.adventureengine.core.GameWorld;
import com.thehxlab.adventureengine.interpreter.GameInterpreter;

import javax.swing.*;

public class Main extends JFrame {
    public static void main(String[] args) {
        Overlays frame = new Overlays();
        frame.setVisible(true);
        try {

            // Compile the game world from the .hsfl file
            String filePath = "adventures/forrest.hsfl";
            GameWorld world = GameCompiler.compile(filePath);

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