package com.thehxlab.adventureengine.GUIs;

import com.thehxlab.adventureengine.GUIs.Player.Knight;
import com.thehxlab.adventureengine.GUIs.lvl.lvlpainter;
import com.thehxlab.adventureengine.core.GameWorld;
import com.thehxlab.adventureengine.core.Player;
import com.thehxlab.adventureengine.core.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Overlays extends JFrame {
    private final GameWorld world;
    private JLayeredPane layeredPane;

    public Overlays(GameWorld world) {
        this.world = world;
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
        reloadUI();
        setVisible(true);
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

        Knight knight = new Knight();
        knight.setSize(knight.getPreferredSize());
        int centerX = (width - knight.getWidth()) / 2;
        int centerY = (height - knight.getHeight()) / 2;
        knight.setLocation(centerX, centerY);
        layeredPane.add(knight, Integer.valueOf(2));

        JPanel stage = new lvlpainter("com/thehxlab/adventureengine/GUIs/lvl/" + roomName + "/stage.png", true, knight);
        stage.setBounds(0, 0, width, height);
        layeredPane.add(stage, Integer.valueOf(1));

        JPanel foreground = new lvlpainter("com/thehxlab/adventureengine/GUIs/lvl/" + roomName + "/foreground.png", false, null);
        foreground.setBounds(0, 0, width, height);
        layeredPane.add(foreground, Integer.valueOf(3));
        System.out.println(roomName);
        Room[] connections = currentRoom.getConnections();

        if (connections[0] != null && currentRoom.isAccessible(0)) {
            JButton goN = new JButton("Norden");
            goN.setBounds((width - 80) / 2, 10, 80, 30);
            goN.addActionListener(e -> {
                player.setCurrentRoom(connections[0]);
                reloadUI();
            });
            layeredPane.add(goN, Integer.valueOf(4));
        }

        if (connections[1] != null && currentRoom.isAccessible(1)) {
            JButton goE = new JButton("Osten");
            goE.setBounds(width - 90, height / 2 - 15, 80, 30);
            goE.addActionListener(e -> {
                player.setCurrentRoom(connections[1]);
                reloadUI();
            });
            layeredPane.add(goE, Integer.valueOf(4));
        }

        if (connections[2] != null && currentRoom.isAccessible(2)) {
            JButton goS = new JButton("Süden");
            goS.setBounds((width - 80) / 2, height - 100, 80, 30);
            goS.addActionListener(e -> {
                player.setCurrentRoom(connections[2]);
                reloadUI();
            });
            layeredPane.add(goS, Integer.valueOf(4));
        }

        if (connections[3] != null && currentRoom.isAccessible(3)) {
            JButton goW = new JButton("Westen");
            goW.setBounds(10, height / 2 - 15, 80, 30);
            goW.addActionListener(e -> {
                player.setCurrentRoom(connections[3]);
                reloadUI();
            });
            layeredPane.add(goW, Integer.valueOf(4));
        }

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

        JPanel UIinv = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        UIinv.setOpaque(false);
        JPanel InvGrid = invcell();
        InvGrid.setPreferredSize(new Dimension(width / 2 - 30, height / 4 - 20));
        UIinv.add(InvGrid);

        JPanel UseGrid = new JPanel(new GridLayout(3, 3, 10, 10));
        UseGrid.setOpaque(false);
        UseGrid.setPreferredSize(new Dimension(width / 2, height / 4 - 20));

        for (String text : new String[]{"Geben", "Nehmen", "Benutze", "Öffne", "Untersuche", "Drücke", "Schließe", "Rede", "Ziehe"}) {
            JButton b = new JButton(text);
            if (text.equals("Geben")) b.setBackground(Color.RED);
            UseGrid.add(b);
        }

        UIPanel.add(UseGrid);
        UIBack.add(UIPanel, BorderLayout.WEST);
        UIBack.add(UIinv, BorderLayout.EAST);
        layeredPane.add(UIBack, Integer.valueOf(5));

        layeredPane.repaint();
        layeredPane.revalidate();
    }

    private JPanel invcell() {
        JPanel InvGrid = new JPanel(new GridLayout(3, 4, 10, 10));
        InvGrid.setOpaque(false);
        for (int i = 0; i < 12; i++) {
            JPanel cell = new JPanel();
            cell.setBackground(new Color(100 + i * 10, 100 + i * 10, 255 - i * 10));
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            InvGrid.add(cell);
        }
        return InvGrid;
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
