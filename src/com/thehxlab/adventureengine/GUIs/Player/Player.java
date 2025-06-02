package com.thehxlab.adventureengine.GUIs.Player;

import javax.swing.*;
import java.awt.*;

public class Player extends JPanel{
    public void player(){
        JPanel player = new JPanel();
        player.setSize(100,250);
        player.setBackground(Color.RED);
        setVisible(true);
    }
}
