package com.kandreyss.visualizer;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.kandreyss.sorts.Sort;

public class SortVisualizer {
    public static Thread startVisualization(Sort sorter, String windowTitle) {
        SortPanel panel = new SortPanel();
        sorter.addListener(panel);

        Thread sortThread = new Thread(() -> {
            sorter.sort();
            SwingUtilities.invokeLater(() -> {
                panel.repaint();
            });
        }, "Sort-Thread");

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame(windowTitle);
            frame.setSize(800, 500);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(panel);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);

            sortThread.start();
        });

        return sortThread;
    }

    public static Thread startVisualiztion(Sort sorter) {
        return startVisualization(sorter, "Sort visualization");
    }
}
