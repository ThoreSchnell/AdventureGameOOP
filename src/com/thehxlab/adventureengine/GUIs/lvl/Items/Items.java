package com.thehxlab.adventureengine.GUIs.lvl.Items;

import com.thehxlab.adventureengine.GUIs.Overlays;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;

public class Items extends JLabel {
    private boolean isInInv;
    private String name;
    private Overlays overlay;

    public Items(String imagePath, String name, Overlays overlay, boolean isInInv,boolean isUsed) {
        super(name);
        this.name = name;
        this.overlay = overlay;
        this.isInInv = isInInv;

        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(imagePath)));
        setIcon(icon);
        setSize(icon.getIconWidth(), icon.getIconHeight());
        setOpaque(false);
        setVisible(true);

        if (!isInInv) {
            setupMouseListener();
        }
        if(isUsed){
            this.remove(this);
        }
    }

    private void setupMouseListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                overlay.itemClicked(Items.this);  // Übergibt das Objekt selbst
            }
        });
    }

    public String getName() {
        return name;
    }

    public boolean isInInv() {
        return isInInv;
    }

    public void setInInv(boolean inInv) {
        this.isInInv = inInv;
    }
}
