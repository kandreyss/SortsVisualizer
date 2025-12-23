package com.kandreyss.sorts.simple;

import com.kandreyss.sorts.Sort;

public class PermutationSort extends Sort {
    public PermutationSort(int[] array) {
        super("Permutations", array, 1);
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();

        if (isSortedWithChecks(tmp)) {
            notifyStep(tmp, -1, -1);
            return tmp;
        }

        notifyStep(tmp, -1, -1);
        while (true) {
            if (Thread.currentThread().isInterrupted()) {
                return tmp;
            }
            boolean hasNext = nextPermutation(tmp);
            if (!hasNext) break;
            notifyStep(tmp, -1, -1);

            if (isSortedWithChecks(tmp)) {
                notifyStep(tmp, -1, -1);
                break;
            }
        }

        notifyStep(tmp, -1, -1);
        return tmp;
    }

    private boolean isSortedWithChecks(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (Thread.currentThread().isInterrupted()) return false;
            metric.incCompairs();
            if (arr[i - 1] > arr[i]) return false;
        }
        return true;
    }

    private boolean nextPermutation(int[] arr) {
        int n = arr.length;
        int i = n - 2;
        while (i >= 0) {
            if (Thread.currentThread().isInterrupted()) return false;
            metric.incCompairs();
            if (arr[i] < arr[i + 1]) break;
            i--;
        }
        if (i < 0) return false;

        int j = n - 1;
        while (j > i) {
            if (Thread.currentThread().isInterrupted()) return false;
            metric.incCompairs();
            if (arr[j] > arr[i]) break;
            j--;
        }

        swap(arr, i, j);
        metric.incSwaps();

        int a = i + 1, b = n - 1;
        while (a < b) {
            if (Thread.currentThread().isInterrupted()) return false;
            swap(arr, a, b);
            metric.incSwaps();
            a++; b--;
        }

        return true;
    }
}