package com.kandreyss.sorts.simple;

import com.kandreyss.sorts.Sort;
import com.kandreyss.utils.Metric;

public class BubbleSort extends Sort {

    public BubbleSort(int[] array) {
        super("Bubble Sort", array, 1);
        metric = new Metric();
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        int n = tmp.length;

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                metric.incCompairs();
                notifyStep(tmp, j, j + 1);
                if (tmp[j] > tmp[j + 1]) {
                    metric.incSwaps();
                    swap(tmp, j, j + 1);
                }
                if (Thread.currentThread().isInterrupted()) {
                    return tmp;
                }
            }
        }
        notifyStep(tmp, -1, -1);
        return tmp;
    }
}