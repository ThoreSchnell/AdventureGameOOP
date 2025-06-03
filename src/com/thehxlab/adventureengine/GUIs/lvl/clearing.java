package com.thehxlab.adventureengine.GUIs.lvl;

import com.thehxlab.adventureengine.GUIs.Player.Player;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.IOException;

public class clearing extends JPanel {
    private final Player player;
    private BufferedImage backgroundImage;
    private Timer moveTimer = null; // Hier, außerhalb des Konstruktors!

    public clearing(String imagePath, boolean enableClickDetection, Player playerToMove) {
        this.player = playerToMove;

        // Bild laden
        try {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
                System.out.println("Bild geladen: " + imagePath);
            } else {
                System.err.println("❌ Bild nicht gefunden: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Fehler beim Laden des Bildes: " + imagePath);
            e.printStackTrace();
        }

        if (enableClickDetection) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    KlickTracker(e, clearing.this.player);
                }
            });
        }

        setOpaque(false);
    }
    public void KlickTracker(MouseEvent e, Player player) {
        if (backgroundImage == null) return;

        int panelWidth = getWidth();
        int panelHeight = getHeight();
        int imageWidth = backgroundImage.getWidth();
        int imageHeight = backgroundImage.getHeight();

        int imgX = e.getX() * imageWidth / panelWidth;
        int imgY = e.getY() * imageHeight / panelHeight;

        if (imgX >= 0 && imgY >= 0 && imgX < imageWidth && imgY < imageHeight) {
            int pixel = backgroundImage.getRGB(imgX, imgY);
            int alpha = (pixel >> 24) & 0xff;

            if (alpha > 0) {
                int targetX = e.getX() - player.getWidth() / 2;
                int targetY = e.getY() - player.getHeight();

                // Vorherigen Timer stoppen, falls noch aktiv
                if (moveTimer != null && moveTimer.isRunning()) {
                    moveTimer.stop();
                }

                animatePlayerTo(player, targetX, targetY);
            }
        }
    }
    private void animatePlayerTo(Player player, int targetX, int targetY) {
        int delay = 10; // ms
        int speed = 4;  // pixel pro Schritt

        moveTimer = new Timer(delay, null);
        moveTimer.addActionListener(evt -> {
            Point current = player.getLocation();
            int dx = targetX - current.x;
            int dy = targetY - current.y;

            // Wenn nahe genug, Bewegung stoppen
            if (Math.abs(dx) < speed && Math.abs(dy) < speed) {
                player.setLocation(targetX, targetY);
                moveTimer.stop();
                return;
            }

            // Neue Position berechnen (ein Schritt)
            int stepX = (int) (speed * Math.signum(dx));
            int stepY = (int) (speed * Math.signum(dy));

            player.setLocation(current.x + stepX, current.y + stepY);
            player.repaint();
        });

        moveTimer.start();
    }




    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
