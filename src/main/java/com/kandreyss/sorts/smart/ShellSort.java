package com.kandreyss.sorts.smart;

import com.kandreyss.sorts.Sort;

public class ShellSort extends Sort {
    public ShellSort(int[] array) {
        super("Shell Sort", array, 1);
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        int n = tmp.length;
        int temp;

        int gap = 1;
        while (gap < n / 3) gap = 3 * gap + 1;

        for (; gap > 0; gap /= 3) {
            for (int i = gap; i < n; i++) {
                if (Thread.currentThread().isInterrupted()) return tmp;

                temp = tmp[i];
                int j = i;
                while (j >= gap) {
                    metric.incCompairs();
                    notifyStep(tmp, j - gap, i);

                    if (tmp[j - gap] > temp) {
                        tmp[j] = tmp[j - gap];
                        metric.incSwaps();
                        notifyStep(tmp, j, j - gap);
                        j -= gap;
                    } else {
                        break;
                    }
                }
                tmp[j] = temp;
                metric.incSwaps();
                notifyStep(tmp, j, i);
            }
        }

        notifyStep(tmp, -1, -1);
        return tmp;
    }
}