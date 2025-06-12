package com.thehxlab.adventureengine.GUIs.lvl;

import com.thehxlab.adventureengine.GUIs.Player.Knight;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.io.IOException;

public class lvlpainter extends JPanel {
    private final Knight knight;
    private BufferedImage backgroundImage;
    private Timer moveTimer = null; // Hier, außerhalb des Konstruktors!

    public lvlpainter(String imagePath, boolean enableClickDetection, Knight knightToMove) {
        this.knight = knightToMove;

        // Bild laden
        try {
            URL imageUrl = getClass().getClassLoader().getResource(imagePath);
            if (imageUrl != null) {
                backgroundImage = ImageIO.read(imageUrl);
                System.out.println("Bild geladen: " + imagePath);
            } else {
                System.err.println("❌< Bild nicht gefunden: " + imagePath);
            }
        } catch (IOException e) {
            System.err.println("⚠️ Fehler beim Laden des Bildes: " + imagePath);
            e.printStackTrace();
        }

        if (enableClickDetection) {
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    KlickTracker(e, lvlpainter.this.knight);
                }
            });
        }

        setOpaque(false);
    }
    public void KlickTracker(MouseEvent e, Knight knight) {
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
                int targetX = e.getX() - knight.getWidth() / 2;
                int targetY = e.getY() - knight.getHeight();

                // Vorherigen Timer stoppen, falls noch aktiv
                if (moveTimer != null && moveTimer.isRunning()) {
                    moveTimer.stop();
                }

                animatePlayerTo(knight, targetX, targetY);
            }
        }
    }
    private void animatePlayerTo(Knight knight, int targetX, int targetY) {
        int delay = 10; // ms
        int speed = 4;  // pixel pro Schritt



        moveTimer = new Timer(delay, null);
        moveTimer.addActionListener(evt -> {
            Point current = knight.getLocation();
            int dx = targetX - current.x;
            int dy = targetY - current.y;

            knight.setWalking(true); // beim Start
            knight.setDirection(dx > 0); // je nach Richtung

            if (dx > 0) {
                knight.setDirection(false); // nach rechts
            } else if (dx < 0) {
                knight.setDirection(true); // nach links
            }


            if (Math.abs(dx) <= speed && Math.abs(dy) <= speed) {
                knight.setLocation(targetX, targetY);
                moveTimer.stop();
                knight.setWalking(false); // beim Stopp
                return;
            }
            // Neue Position berechnen (ein Schritt)
            int stepX = 0;
            int stepY = 0;

            if (Math.abs(dx) > speed) {
                stepX = (int) (speed * Math.signum(dx));
            } else {
                stepX = dx; // letzter kleiner Schritt
            }

            if (Math.abs(dy) > speed) {
                stepY = (int) (speed * Math.signum(dy));
            } else {
                stepY = dy; // letzter kleiner Schritt
            }

            knight.setLocation(current.x + stepX, current.y + stepY);
            knight.repaint();
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
