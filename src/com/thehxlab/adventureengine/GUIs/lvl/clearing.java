package com.thehxlab.adventureengine.GUIs.lvl;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class clearing extends JPanel {
    private Image backgroundImage;

    public clearing(String imagePath) {
        // Bild über ClassLoader laden
        try {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = new ImageIcon(imageUrl).getImage();
                System.out.println("Bild geladen: " + imagePath);
            } else {
                System.err.println("❌ Bild nicht gefunden: " + imagePath);
            }
        } catch (Exception e) {
            System.err.println("⚠️ Fehler beim Laden des Bildes: " + imagePath);
            e.printStackTrace();
        }

        // Transparenz erlauben (für LayeredPane)
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
