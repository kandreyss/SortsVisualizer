package com.kandreyss;

import javax.swing.SwingUtilities;
import com.kandreyss.visualizer.menu.Menu;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Menu menu = Menu.createWithDefaults();
            menu.show();
        });
    }
}