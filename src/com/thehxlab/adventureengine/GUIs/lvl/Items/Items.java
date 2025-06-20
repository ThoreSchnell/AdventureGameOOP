package com.thehxlab.adventureengine.GUIs.lvl.Items;

import com.thehxlab.adventureengine.GUIs.Overlays;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Items extends JLabel {
    private String name;
    private Overlays overlay;

    public Items(String imagePath, String name, Overlays overlay, boolean isInInv) {
        super(name);
        this.name = name;
        this.overlay = overlay;

        ImageIcon icon = new ImageIcon(imagePath);

        setIcon(icon);
        setSize(icon.getIconWidth(), icon.getIconHeight());
        setOpaque(false);
        setVisible(true);

        if (!isInInv) {
            setupMouseListener();
        }
    }

    private void setupMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                overlay.itemClicked(name);
            }
        });
    }
}
