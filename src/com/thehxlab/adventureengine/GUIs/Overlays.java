package com.thehxlab.adventureengine.GUIs;

import com.thehxlab.adventureengine.GUIs.lvl.InventoryPanel;
import com.thehxlab.adventureengine.interpreter.CommandExecutor;
import com.thehxlab.adventureengine.GUIs.Player.Knight;
import com.thehxlab.adventureengine.GUIs.lvl.lvlpainter;
import com.thehxlab.adventureengine.core.GameWorld;
import com.thehxlab.adventureengine.core.Player;
import com.thehxlab.adventureengine.core.Room;
import com.thehxlab.adventureengine.GUIs.lvl.Items.Items;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Overlays extends JFrame {
    private InventoryPanel inventoryPanel;
    private final List<Items> items = new ArrayList<>();
    private Items selectedItem = null;
    private String selectedAction = null;

    private CommandExecutor commandExecutor;
    private final GameWorld world;
    private JLayeredPane layeredPane;

    private Knight knight;

    public Overlays(GameWorld world) {
        this.world = world;
        this.commandExecutor = new CommandExecutor(world);
        setTitle("Game");
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(this);
        } else {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setVisible(true);
        }

        addWindowFocusListener(new WindowAdapter() {
            boolean focus = false;
            @Override
            public void windowLostFocus(WindowEvent e) {
                if (focus) {
                    System.out.println("Fokus verloren");
                    focus = false;
                }
            }
            @Override
            public void windowGainedFocus(WindowEvent e) {
                focus = true;
                setVisible(true);
                System.out.println("Fokus wieder da");
            }
        });

        layeredPane = new JLayeredPane();
        add(layeredPane);

        initializeItems();

        this.inventoryPanel = new InventoryPanel(items);

        knight = new Knight();
        knight.setSize(knight.getPreferredSize());

        GraphicsDevice gd2 = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd2.getDisplayMode().getWidth();
        int height = gd2.getDisplayMode().getHeight();
        int startX = (width - knight.getWidth()) / 2;
        int startY = (height - knight.getHeight()) / 2;
        knight.setLocation(startX, startY);

        reloadUI();
        setVisible(true);
    }

    private void initializeItems() {
        items.add(new Items("/com/thehxlab/adventureengine/GUIs/lvl/Items/Seil.png", "Seil", this, false,false));
        items.add(new Items("/com/thehxlab/adventureengine/GUIs/lvl/Items/Brett.png","Brett",this,false,false));
    }

    private void reloadUI() {
        layeredPane.removeAll();

        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();

        Player player = world.getPlayer();
        Room currentRoom = player.getCurrentRoom();
        String roomName = currentRoom.getName().toLowerCase();

        JPanel background = new lvlpainter("com/thehxlab/adventureengine/GUIs/lvl/" + roomName + "/background.png", false, null);
        background.setBounds(0, 0, width, height);
        layeredPane.add(background, Integer.valueOf(0));

        layeredPane.add(knight, Integer.valueOf(2));

        JPanel stage = new lvlpainter("com/thehxlab/adventureengine/GUIs/lvl/" + roomName + "/stage.png", true, knight);
        stage.setBounds(0, 0, width, height);
        layeredPane.add(stage, Integer.valueOf(1));

        JPanel foreground = new lvlpainter("com/thehxlab/adventureengine/GUIs/lvl/" + roomName + "/foreground.png", false, null);
        foreground.setBounds(0, 0, width, height);
        layeredPane.add(foreground, Integer.valueOf(3));

        for (Items item : items) {
            if (!item.isInInv()) {
                if(item.getName().equals("Seil"))item.setLocation(width/3,height/3+200);
                if(item.getName().equals("Brett"))item.setLocation(width/3*2,height/3+200);
                layeredPane.add(item, Integer.valueOf(2));
            }
        }

        addNavigationButtons(width, height);
        addUIElements(width, height);

        inventoryPanel.refresh();
        layeredPane.repaint();
        layeredPane.revalidate();
    }

    public void itemClicked(Items item) {
        selectedItem = item;
        System.out.println("Item ausgewählt: " + item.getName());
        checkAndExecute();
    }

    private void addNavigationButtons(int width, int height) {
        JButton goN = new JButton("Norden");
        goN.setBounds((width - 80) / 2, 10, 80, 30);
        goN.addActionListener(e -> {
            commandExecutor.execute("go", "North");
            reloadUI();
        });
        layeredPane.add(goN, Integer.valueOf(4));

        JButton goE = new JButton("Osten");
        goE.setBounds(width - 90, height / 2 - 15, 80, 30);
        goE.addActionListener(e -> {
            commandExecutor.execute("go", "East");
            reloadUI();
        });
        layeredPane.add(goE, Integer.valueOf(4));

        JButton goS = new JButton("Süden");
        goS.setBounds((width - 80) / 2, height - 100, 80, 30);
        goS.addActionListener(e -> {
            commandExecutor.execute("go", "South");
            reloadUI();
        });
        layeredPane.add(goS, Integer.valueOf(4));

        JButton goW = new JButton("Westen");
        goW.setBounds(10, height / 2 - 15, 80, 30);
        goW.addActionListener(e -> {
            commandExecutor.execute("go", "West");
            reloadUI();
        });
        layeredPane.add(goW, Integer.valueOf(4));
    }

    private void addUIElements(int width, int height) {
        JPanel UITop = new JPanel(new BorderLayout());
        UITop.setBounds(0, 0, width, height / 12);
        JButton settings = new JButton("Einstellungen");
        settings.setPreferredSize(new Dimension(150, 30));
        settings.addActionListener(e -> Settings(this));
        UITop.add(settings, BorderLayout.WEST);
        layeredPane.add(UITop, Integer.valueOf(5));

        JPanel UIBack = new JPanel(new BorderLayout());
        UIBack.setBounds(0, height - height / 4, width, height / 4);
        UIBack.setBackground(new Color(133, 133, 133));

        JPanel UIPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        UIPanel.setOpaque(false);
        UIPanel.add(getActionPanel(width, height));

        JPanel UIinv = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        UIinv.setBackground(new Color(133, 133, 133));
        UIinv.add(inventoryPanel);

        UIBack.add(UIPanel, BorderLayout.WEST);
        UIBack.add(UIinv, BorderLayout.EAST);
        layeredPane.add(UIBack, Integer.valueOf(5));
    }

    private JPanel getActionPanel(int width, int height) {
        JPanel panel = new JPanel(new GridLayout(3, 3, 10, 10));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(width / 2, height / 4 - 20));

        String[] actions = {"Geben", "Nehmen", "Benutze", "Öffne", "Untersuche", "Drücke", "Kombiniere", "Rede", "Ziehe"};
        for (String action : actions) {
            JButton button = new JButton(action);
            button.addActionListener(e -> {
                selectedAction = action;
                System.out.println("Aktion ausgewählt: " + selectedAction);
                checkAndExecute();
            });
            panel.add(button);
        }
        return panel;
    }

    private void checkAndExecute() {
        if (selectedAction == null) return;

        if (selectedAction.equals("Kombiniere")) {
            List<Items> selected = inventoryPanel.getSelectedItems();
            if (selected.size() == 2) {
                Items item1 = selected.get(0);
                Items item2 = selected.get(1);
                System.out.println("Kombiniere: " + item1.getName() + " + " + item2.getName());

                if ((item1.getName().equals("Seil") && item2.getName().equals("Brett"))
                        || (item1.getName().equals("Brett") && item2.getName().equals("Seil"))) {
                    System.out.println("Du hast eine Brücke gebaut!");

                    items.remove(item1);
                    items.remove(item2);

                    // Beispiel: neues Item erzeugen
                    //Items bruecke = new Items("/com/thehxlab/adventureengine/GUIs/lvl/Items/Bruecke.png", "Brücke", this, true, false);
                    //items.add(bruecke);

                    inventoryPanel.refresh();
                    reloadUI();
                } else {
                    System.out.println("Diese Kombination ergibt keinen Sinn.");
                }

                inventoryPanel.clearSelection();
            } else {
                System.out.println("Du musst zwei Items auswählen.");
            }
        }
        else if (selectedAction.equals("Nehmen") && selectedItem != null) {
            commandExecutor.execute("take", selectedItem.getName());
            selectedItem.setInInv(true);
            inventoryPanel.refresh();
            reloadUI();
        }

        selectedAction = null;
        selectedItem = null;
    }


    private void Settings(JFrame parent) {
        JPanel glassPane = new JPanel();
        glassPane.setBackground(new Color(0, 0, 0, 150));
        glassPane.setLayout(null);
        glassPane.setOpaque(true);
        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {});
        parent.setGlassPane(glassPane);
        glassPane.setVisible(true);

        JWindow settingsWindow = new JWindow(parent);
        settingsWindow.setSize(400, 200);
        settingsWindow.setLocationRelativeTo(parent);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        contentPanel.setBackground(Color.LIGHT_GRAY);

        JButton backButton = new JButton("Zurück");
        backButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        backButton.addActionListener(e -> {
            settingsWindow.dispose();
            parent.setGlassPane(new JPanel());
            parent.getGlassPane().setVisible(false);
        });

        JButton exitButton = new JButton("Spiel beenden");
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitButton.addActionListener(e -> System.exit(0));

        contentPanel.add(backButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(exitButton);

        settingsWindow.add(contentPanel);
        settingsWindow.setAlwaysOnTop(true);
        settingsWindow.setVisible(true);
    }
}
