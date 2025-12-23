package com.kandreyss.sorts.simple;

import com.kandreyss.sorts.Sort;

public class SelectionSort extends Sort {
    public SelectionSort(int[] array) {
        super("Selection Sort", array, 1);
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        int n = tmp.length;
        int minIdx = 0;

        for(int i = 0; i < n - 1; i++) {
            minIdx = i;
            for(int j = i + 1; j < n; j++) {
                metric.incCompairs();
                notifyStep(tmp, i, minIdx);
                if(tmp[j] < tmp[minIdx])
                    minIdx = j;             
            }
            metric.incSwaps();
            swap(tmp, i, minIdx);
        }

        return tmp;
    }
}
