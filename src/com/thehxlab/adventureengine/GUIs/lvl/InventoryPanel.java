package com.thehxlab.adventureengine.GUIs.lvl;

import com.thehxlab.adventureengine.GUIs.lvl.Items.Items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class InventoryPanel extends JPanel {
    private static final int ROWS = 2;
    private static final int COLS = 4;
    private static final int TOTAL_SLOTS = ROWS * COLS;

    private final List<Items> items;
    private final List<Items> selectedItems = new ArrayList<>();

    public InventoryPanel(List<Items> items) {
        this.items = items;
        setLayout(new GridLayout(ROWS, COLS, 10, 10));
        setOpaque(true);
        setBackground(new Color(200, 200, 200)); // fester Hintergrund

        // Feste Größe
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth() / 2 - 50;
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
                JPanel cell = createItemCell(item);
                add(cell);
                filledSlots++;
            }
        }

        for (int i = filledSlots; i < TOTAL_SLOTS; i++) {
            JPanel emptyCell = new JPanel();
            emptyCell.setBackground(new Color(220, 220, 220));
            emptyCell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            add(emptyCell);
        }

        revalidate();
        repaint();
    }

    private JPanel createItemCell(Items item) {
        JPanel cell = new JPanel(new BorderLayout());
        cell.setOpaque(false);
        JLabel iconLabel = new JLabel(item.getIcon());
        cell.add(iconLabel, BorderLayout.CENTER);
        cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Auswahl-Logik
        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                toggleSelection(item, cell);
            }
        });

        return cell;
    }

    private void toggleSelection(Items item, JPanel cell) {
        if (selectedItems.contains(item)) {
            selectedItems.remove(item);
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        } else {
            if (selectedItems.size() < 2) { // Maximal zwei
                selectedItems.add(item);
                cell.setBorder(BorderFactory.createLineBorder(Color.RED, 3));
            }
        }
    }

    public List<Items> getSelectedItems() {
        return selectedItems;
    }

    public void clearSelection() {
        selectedItems.clear();
        refresh();
    }
}
