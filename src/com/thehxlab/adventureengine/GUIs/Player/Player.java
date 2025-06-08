package com.thehxlab.adventureengine.GUIs.Player;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Player extends JPanel {
    private final ImageIcon knightIdle;
    private final ImageIcon knightRightGif;
    private final ImageIcon knightLeftGif;
    private ImageIcon currentGif;


    private boolean facingRight = true;
    private boolean isWalking = false;

    public Player() {
        knightIdle = new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/thehxlab/adventureengine/GUIs/Player/Knight-Idle.gif")));
        knightRightGif = new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/thehxlab/adventureengine/GUIs/Player/Knight-left.gif")));
        knightLeftGif = new ImageIcon(Objects.requireNonNull(getClass().getResource("/com/thehxlab/adventureengine/GUIs/Player/Knight-right.gif")));
        currentGif = knightIdle; // Standard: nach rechts

        currentGif = knightIdle;

        setOpaque(false);
        setPreferredSize(new Dimension(currentGif.getIconWidth(), currentGif.getIconHeight()));
        setLayout(null);
    }

    public void setDirection(boolean right) {
        facingRight = right;
        updateGif();
    }

    public void setWalking(boolean walking) {
        isWalking = walking;
        updateGif();
    }

    private void updateGif() {
        if (isWalking) {
            currentGif = facingRight ? knightRightGif : knightLeftGif;
        } else {
            currentGif = knightIdle;
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        currentGif.paintIcon(this, g, 0, 0);
    }
}
