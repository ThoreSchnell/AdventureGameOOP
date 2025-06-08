package com.thehxlab.adventureengine.GUIs;

import com.thehxlab.adventureengine.GUIs.Player.Player;
import com.thehxlab.adventureengine.GUIs.lvl.clearing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class Overlays extends JFrame {
    public Overlays(){

        JFrame frame = new JFrame("Game");
        frame.setLayout(new BorderLayout());

        frame.addWindowFocusListener(new WindowAdapter() {
            boolean focus = false;
            @Override
            public void windowLostFocus(WindowEvent e) {
                if(focus) {
                    System.out.println("Fokus verloren");
                    focus=false;
                }
            }

            @Override
            public void windowGainedFocus(WindowEvent e) {
                focus=true;
                System.out.println("Fokus wieder da");
            }
        });


        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();

        // JFrame im Vollbild anzeigen
        if (gd.isFullScreenSupported()) {
            gd.setFullScreenWindow(frame);
        } else {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            frame.setVisible(true);
        }
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton GiveB = new JButton("Geben");
        JButton PickB = new JButton("Nehmen");
        JButton UseB = new JButton("Benutze");
        JButton OpenB = new JButton("Öffne");
        JButton LookB = new JButton("Untersuche");
        JButton PushB = new JButton("Drücke");
        JButton CloseB = new JButton("Schließe");
        JButton TalkB = new JButton("Rede");
        JButton PullB = new JButton("Ziehe");
        JButton SettingsB = new JButton("Einstellungen");

        SettingsB.setPreferredSize(new Dimension(150, 30));

        SettingsB.addActionListener(e -> Settings(frame));
        GiveB.setBackground(Color.RED);
        GiveB.setOpaque(true);


        JLayeredPane layeredPane=Layer(frame.getWidth(),frame.getHeight());




        

        JPanel UITop =new JPanel(new BorderLayout());
        UITop.setPreferredSize(new Dimension(1000,frame.getHeight()/12));

        JPanel UIBack = new JPanel(new BorderLayout());
        UIBack.setBackground(new Color(133, 133, 133));
        UIBack.setPreferredSize(new Dimension(1000, frame.getHeight()/4)); // feste Höhe

        // Rechts unten: Button-Panel
        JPanel UIPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        UIPanel.setOpaque(false); // damit UIBack-Hintergrund sichtbar bleibt


        JPanel UIinv = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        UIinv.setOpaque(false);


        JPanel InvGrid = invcell();
        UIinv.setPreferredSize(new Dimension(-20+frame.getWidth()/2,frame.getHeight()/4));
        InvGrid.setPreferredSize(new Dimension(-30+frame.getWidth()/2,-20+frame.getHeight()/4));
        UIinv.add(InvGrid);


        JPanel UseGrid = new JPanel(new GridLayout(3, 3, 10, 10));
        UseGrid.setOpaque(false); // ebenfalls durchscheinend
        UseGrid.setPreferredSize(new Dimension(frame.getWidth()/2, -20+frame.getHeight()/4)); // feste Größe

        // Buttons ins Grid
        UseGrid.add(GiveB);
        UseGrid.add(PickB);
        UseGrid.add(UseB);
        UseGrid.add(OpenB);
        UseGrid.add(LookB);
        UseGrid.add(PushB);
        UseGrid.add(CloseB);
        UseGrid.add(TalkB);
        UseGrid.add(PullB);

        UIPanel.add(UseGrid);
        UIBack.add(UIinv,BorderLayout.EAST);
        UIBack.add(UIPanel, BorderLayout.WEST); // Buttons nach unten rechts
        UITop.add(SettingsB, BorderLayout.WEST);

        frame.add(UITop, BorderLayout.NORTH);
        frame.add(layeredPane,BorderLayout.CENTER);
        frame.add(UIBack, BorderLayout.SOUTH);

        layeredPane.repaint();
        frame.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    private JPanel invcell() {
        JPanel InvGrid = new JPanel(new GridLayout(3,4,10,10));
        InvGrid.setOpaque(false);


        for (int i = 0; i < 12; i++) {
            JPanel cell = new JPanel();
            cell.setBackground(new Color(100 + i * 10, 100 + i * 10, 255 - i * 10)); // Farbe
            cell.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Umrandung
            InvGrid.add(cell);
        }

        return InvGrid;
    }

    private JLayeredPane Layer(int width, int height) {
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(width, height-height/5));
        layeredPane.setLayout(null); // wichtig: kein Layout-Manager

        JPanel background = new clearing("com/thehxlab/adventureengine/GUIs/lvl/clearing/background.png",false, null);
        background.setBounds(0, 0, layeredPane.getPreferredSize().width, 20+layeredPane.getPreferredSize().height);
        layeredPane.add(background, Integer.valueOf(0));

        Player player = new Player();
        player.setSize(player.getPreferredSize());
        int centerX = (layeredPane.getPreferredSize().width - player.getWidth()) / 2;
        int centerY = (layeredPane.getPreferredSize().height - player.getHeight()) / 2;
        player.setLocation(centerX, centerY);
        layeredPane.add(player, Integer.valueOf(2));

        JPanel ForegroundPanel = new clearing("com/thehxlab/adventureengine/GUIs/lvl/clearing/foreground.png",false, null);
        ForegroundPanel.setBounds(0, 0, layeredPane.getPreferredSize().width, 20+layeredPane.getPreferredSize().height);
        layeredPane.add(ForegroundPanel, Integer.valueOf(3));

        JPanel playerstage = new clearing("com/thehxlab/adventureengine/GUIs/lvl/clearing/stage.png",true,player);
        playerstage.setBounds(0, 0, layeredPane.getPreferredSize().width, 20+layeredPane.getPreferredSize().height);
        layeredPane.add(playerstage, Integer.valueOf(1));
            layeredPane.setVisible(true);
        return layeredPane;
    }

    private void Settings(JFrame parent) {
        // Neues GlassPane erstellen
        JPanel glassPane = new JPanel();
        glassPane.setBackground(new Color(0, 0, 0, 150)); // halbtransparent schwarz
        glassPane.setLayout(null);
        glassPane.setOpaque(true);

        // Blockiert Mausinteraktion mit Komponenten darunter
        glassPane.addMouseListener(new java.awt.event.MouseAdapter() {});
        parent.setGlassPane(glassPane);
        glassPane.setVisible(true); // jetzt sichtbar machen

        // Popup-Fenster
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
            parent.setGlassPane(new JPanel()); // leeres Panel setzen
            parent.getGlassPane().setVisible(false); // sicherstellen, dass es unsichtbar ist
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
