package com.kandreyss.sorts;

import java.util.ArrayList;

import com.kandreyss.utils.Metric;

public abstract class Sort {
    protected String name;
    protected Metric metric = new Metric();
    protected int[] array;

    private final ArrayList<SortListener> listeners = new ArrayList<>();
    private int delayMillis = 0;

    public Sort(String name, int[] array) {
        this.name = name;
        this.array = array;
    }

    public Sort(String name, int[] array, int delayMillis) {
        this.name = name;
        this.array = array;
        this.delayMillis = Math.max(0, delayMillis);
    }

    public void addListener(SortListener listener) {
        listeners.add(listener);
    }

    protected void notifyStep(int[] tmpArray, int index1, int index2) {
        int[] snapshot = tmpArray.clone();
        long compairs = metric.getCompairs();
        long swaps = metric.getSwaps();
        for (SortListener l : listeners) {
            l.onStep(snapshot, index1, index2, compairs, swaps);
        }

        if (delayMillis > 0) {
            try { Thread.sleep(delayMillis); }
            catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        }
    }

    protected void swap(int[] arr, int i, int j) {
        int t = arr[j];
        arr[j] = arr[i];
        arr[i] = t;

        notifyStep(arr, i, j);
    }

    public abstract int[] sort();

    public void setDelay(int delay) {
        delayMillis = delay;
    }

    protected Metric getMetric() { return metric; }
    protected int[] getArray() { return array; }
    public String getName() { return name; }
}