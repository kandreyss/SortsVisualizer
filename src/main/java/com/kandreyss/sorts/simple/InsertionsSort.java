package com.kandreyss.sorts.simple;

import com.kandreyss.sorts.Sort;

public class InsertionsSort extends Sort {
    public InsertionsSort(int[] array) {
        super("Insertion Sort", array, 1);
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        int n = tmp.length;

        for (int i = 1; i < n; i++) {
            int j = i;
            while (j > 0) {
                metric.incCompairs();
                notifyStep(tmp, j, j - 1);

                if (tmp[j] < tmp[j - 1]) {
                    metric.incSwaps();
                    swap(tmp, j, j - 1);
                } else {
                    break;
                }
                j--;
            }
        }
        notifyStep(tmp, -1, -1);
        return tmp;
    }
}