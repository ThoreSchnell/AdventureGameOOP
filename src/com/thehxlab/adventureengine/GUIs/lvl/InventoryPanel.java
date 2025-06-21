package com.thehxlab.adventureengine.GUIs.lvl;

import com.thehxlab.adventureengine.GUIs.lvl.Items.Items;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {
    private static final int ROWS = 2;
    private static final int COLS = 3;
    private static final int TOTAL_SLOTS = ROWS * COLS;

    private final List<Items> items;

    public InventoryPanel(List<Items> items) {
        this.items = items;
        setLayout(new GridLayout(2, 3, 10, 10));
        setOpaque(false);
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth()/2-50;
        int height = 250;

        Dimension fixedSize = new Dimension(width, height);
        setPreferredSize(fixedSize);
        setMinimumSize(fixedSize);
        setMaximumSize(fixedSize);

        refresh();
    }

    public void refresh() {
        removeAll();

        int filledSlots = 0;

        for (Items item : items) {
            if (item.isInInv()) {
                JPanel cell = new JPanel(new BorderLayout());
                cell.setOpaque(false);
                JLabel iconLabel = new JLabel(item.getIcon());
                cell.add(iconLabel, BorderLayout.CENTER);
                cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                add(cell);
                filledSlots++;
            }
        }

        // Leere Felder auffüllen
        for (int i = filledSlots; i < TOTAL_SLOTS; i++) {
            JPanel emptyCell = new JPanel();
            emptyCell.setBackground(new Color(220, 220, 220));
            emptyCell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(emptyCell);
        }

        revalidate();
        repaint();
    }
}
