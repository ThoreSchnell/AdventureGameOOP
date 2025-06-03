package com.thehxlab.adventureengine.GUIs.Player;

import javax.swing.*;
import java.awt.*;

public class Player extends JPanel {

    public Player() {
        // Setze feste Größe auf 100x250
        setSize(100, 250);
        setBackground(Color.RED); // Hintergrundfarbe Rot
        setOpaque(true); // wichtig, damit Hintergrund sichtbar ist
    }
}
