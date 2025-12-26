package com.kandreyss.visualizer.menu;

import com.kandreyss.sorts.Sort;
import com.kandreyss.sorts.simple.BubbleSort;
import com.kandreyss.sorts.simple.InsertionsSort;
import com.kandreyss.sorts.simple.PermutationSort;
import com.kandreyss.sorts.simple.SelectionSort;
import com.kandreyss.sorts.smart.HeapSort;
import com.kandreyss.sorts.smart.MergeSort;
import com.kandreyss.sorts.smart.QuickSort;
import com.kandreyss.sorts.smart.ShellSort;
import com.kandreyss.utils.Shuffler;
import com.kandreyss.visualizer.SortPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class Menu {
    private final JFrame frame;
    private final JComboBox<String> algoBox;
    private final JComboBox<Integer> sizeBox;
    private final JSpinner delaySpinner;
    private final JCheckBox exitOnFinish;
    private final JButton startBtn;
    private final JButton stopBtn;
    private final JButton exitBtn;

    private final SortPanel panel;
    private final Map<String, Function<int[], Sort>> algorithmFactories;

    private volatile Thread sortThread;

    public Menu(Map<String, Function<int[], Sort>> algorithmFactories, SortPanel panel) {
        this.algorithmFactories = new LinkedHashMap<>(Objects.requireNonNull(algorithmFactories, "algorithmFactories"));
        this.panel = Objects.requireNonNull(panel, "panel");

        if (!SwingUtilities.isEventDispatchThread()) {
            final JFrame[] holder = new JFrame[1];
            try {
                SwingUtilities.invokeAndWait(() -> holder[0] = buildFrame());
            } catch (Exception e) {
                throw new RuntimeException("Failed to build UI", e);
            }
            frame = holder[0];
        } else {
            frame = buildFrame();
        }

        algoBox = findComboBox(frame, "algoBox", String.class);
        sizeBox = findComboBox(frame, "sizeBox", Integer.class);
        delaySpinner = findComponent(frame, "delaySpinner", JSpinner.class);
        exitOnFinish = findCheckBox(frame, "exitOnFinish");
        startBtn = findButton(frame, "startBtn");
        stopBtn = findButton(frame, "stopBtn");
        exitBtn = findButton(frame, "exitBtn");

        startBtn.addActionListener(this::onStart);
        stopBtn.addActionListener(e -> stopSorting());
        exitBtn.addActionListener(e -> {
            stopSorting();
            frame.dispose();
        });
    }

    public static Menu createWithDefaults() {
        Map<String, Function<int[], Sort>> factories = new LinkedHashMap<>();
        factories.put("Bubble", BubbleSort::new);
        factories.put("Insertion", InsertionsSort::new);
        factories.put("Selection", SelectionSort::new);
        factories.put("Permutations", PermutationSort::new);
        factories.put("Merge", MergeSort::new);
        factories.put("Quick", QuickSort::new);
        factories.put("Shell", ShellSort::new);
        factories.put("Heap", HeapSort::new);
        SortPanel panel = new SortPanel();
        return new Menu(factories, panel);
    }

    public void show() {
        if (SwingUtilities.isEventDispatchThread()) {
            frame.setVisible(true);
        } else {
            SwingUtilities.invokeLater(() -> frame.setVisible(true));
        }
    }

    public SortPanel getSortPanel() {
        return panel;
    }

    public void stopSorting() {
        Thread t = sortThread;
        if (t != null && t.isAlive()) {
            t.interrupt();
            try {
                t.join(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        sortThread = null;

        SwingUtilities.invokeLater(() -> {
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
        });
    }

    private JFrame buildFrame() {
        JFrame f = new JFrame("Sort Visualizer - Menu");
        f.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        f.setLayout(new BorderLayout(8, 8));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));

        JComboBox<String> algo = new JComboBox<>(algorithmFactories.keySet().toArray(new String[0]));
        algo.setName("algoBox");

        JComboBox<Integer> size = new JComboBox<>(new Integer[]{10, 50, 100, 150, 200, 500, 1000});
        size.setName("sizeBox");

        JSpinner delay = new JSpinner(new SpinnerNumberModel(20, 0, 2000, 10));
        delay.setName("delaySpinner");
        delay.setToolTipText("Delay between animation steps (milliseconds)");

        JCheckBox exit = new JCheckBox("Exit on finish", false);
        exit.setName("exitOnFinish");

        JButton start = new JButton("Start");
        start.setName("startBtn");

        JButton stopButton = new JButton("Stop");
        stopButton.setName("stopBtn");
        stopButton.setEnabled(false);

        JButton exitButton = new JButton("Exit");
        exitButton.setName("exitBtn");

        top.add(new JLabel("Algorithm:"));
        top.add(algo);
        top.add(new JLabel("Size:"));
        top.add(size);
        top.add(new JLabel("Delay ms:"));
        top.add(delay);
        top.add(exit);
        top.add(start);
        top.add(stopButton);
        top.add(exitButton);

        f.add(top, BorderLayout.NORTH);
        f.add(panel, BorderLayout.CENTER);

        f.setSize(1000, 640);
        f.setLocationRelativeTo(null);

        return f;
    }

    @SuppressWarnings("unchecked")
    private <T extends JComponent> T findComponent(Container c, String name, Class<T> cls) {
        for (Component comp : c.getComponents()) {
            if (name.equals(comp.getName()) && cls.isInstance(comp)) {
                return (T) comp;
            }
            if (comp instanceof Container) {
                T child = findComponent((Container) comp, name, cls);
                if (child != null) return child;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private <E> JComboBox<E> findComboBox(Container c, String name, Class<E> elementClass) {
        return (JComboBox<E>) findComponent(c, name, JComboBox.class);
    }

    private JCheckBox findCheckBox(Container c, String name) {
        return findComponent(c, name, JCheckBox.class);
    }

    private JButton findButton(Container c, String name) {
        return findComponent(c, name, JButton.class);
    }

    private void onStart(ActionEvent e) {
        startBtn.setEnabled(false);
        stopBtn.setEnabled(true);

        String algoName = (String) algoBox.getSelectedItem();
        int size = (Integer) sizeBox.getSelectedItem();
        int delay = (Integer) delaySpinner.getValue();

        Function<int[], Sort> factory = algorithmFactories.get(algoName);
        if (factory == null) {
            JOptionPane.showMessageDialog(frame, "Algorithm not available: " + algoName, "Error", JOptionPane.ERROR_MESSAGE);
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            return;
        }

        int[] array = Shuffler.randomArray(size);
        Sort sorter;
        try {
            sorter = factory.apply(array);
            applyDelayIfPossible(sorter, delay);
        } catch (Throwable ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Failed to create sorter: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            startBtn.setEnabled(true);
            stopBtn.setEnabled(false);
            return;
        }

        try {
            sorter.addListener(panel);
        } catch (Throwable ignored) {
        }

        panel.onStep(array.clone(), -1, -1, 0L, 0L);

        Thread t = new Thread(() -> {
            try {
                sorter.sort();
                SwingUtilities.invokeLater(() -> {
                    startBtn.setEnabled(true);
                    stopBtn.setEnabled(false);
                    JOptionPane.showMessageDialog(frame, "Sorting finished: " + sorter.getName());
                    if (exitOnFinish.isSelected()) {
                        try { Thread.sleep(200); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
                        frame.dispose();
                    }
                });
            } catch (Throwable ex) {
                ex.printStackTrace();
                SwingUtilities.invokeLater(() -> {
                    JOptionPane.showMessageDialog(frame, "Error during sort: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    startBtn.setEnabled(true);
                    stopBtn.setEnabled(false);
                });
            }
        }, "Sort-Thread");

        sortThread = t;
        t.start();
    }

    private void applyDelayIfPossible(Sort sorter, int delay) {
        if (sorter == null) return;
        String[] names = {"setDelayMillis", "setDelay", "setDelayMs"};
        for (String name : names) {
            try {
                Method m = sorter.getClass().getMethod(name, int.class);
                m.setAccessible(true);
                m.invoke(sorter, delay);
                return;
            } catch (NoSuchMethodException ignored) {
            } catch (Exception ignored) {
                return;
            }
        }
    }
}