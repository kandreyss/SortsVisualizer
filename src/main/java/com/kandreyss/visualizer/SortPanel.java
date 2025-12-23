package com.kandreyss.visualizer;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

import com.kandreyss.sorts.SortListener;

public class SortPanel extends JPanel implements SortListener {
    private int[] array = new int[0];
    private int index1 = -1, index2 = -1;
    private long compairs = 0L;
    private long swaps = 0L;

    public SortPanel() {
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(800, 400));
    }

    @Override
    public void onStep(int[] arraySnapshot, int index1, int index2, long compairs, long swaps) {
        SwingUtilities.invokeLater(() -> {
            this.array = arraySnapshot;
            this.index1 = index1;
            this.index2 = index2;
            this.compairs = compairs;
            this.swaps = swaps;
            repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (array == null || array.length == 0) return;

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int n = array.length;
            double width = getWidth() / (double) n;
            int max = 1;
            for (int v : array) if (v > max) max = v;

            boolean noGap = n > 150;
            for (int i = 0; i < n; i++) {
                double h = (array[i] / (double) max) * getHeight() * 0.8;
                double x = i * width;
                double y = getHeight() - h;

                if (i == index1 || i == index2) g2.setColor(Color.RED);
                else g2.setColor(Color.BLUE);

                double rectW = noGap ? Math.max(1, width) : Math.max(1, width - 2);
                Rectangle2D r = new Rectangle2D.Double(x, y, rectW, h);
                g2.fill(r);
            }
            g2.setColor(Color.BLACK);
            g2.setFont(g2.getFont().deriveFont(Font.BOLD, 14f));
            String metrics = String.format("Comparisons: %d    Swaps: %d", compairs, swaps);
            g2.drawString(metrics, 8, 18);
        } finally {
            g2.dispose();
        }
    }
}