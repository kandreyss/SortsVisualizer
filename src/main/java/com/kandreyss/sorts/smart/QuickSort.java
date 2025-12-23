package com.kandreyss.sorts.smart;

import com.kandreyss.sorts.Sort;

public class QuickSort extends Sort {
    public QuickSort(int[] array) {
        super("QuickSort", array, 1);
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        quicksort(tmp, 0, tmp.length - 1);
        notifyStep(tmp, -1, -1);
        return tmp;
    }

    private void quicksort(int[] arr, int left, int right) {
        if (Thread.currentThread().isInterrupted()) return;
        if (left < right) {
            int p = partitionHoare(arr, left, right);
            quicksort(arr, left, p);
            quicksort(arr, p + 1, right);
        }
    }

    private int partitionHoare(int[] arr, int left, int right) {
        int pivotIndex = left + (right - left) / 2;
        int pivot = arr[pivotIndex];

        int i = left - 1;
        int j = right + 1;

        notifyStep(arr, pivotIndex, -1);

        while (true) {
            do {
                i++;
                metric.incCompairs();
                notifyStep(arr, i, pivotIndex);
            } while (arr[i] < pivot);

            do {
                j--;
                metric.incCompairs();
                notifyStep(arr, j, pivotIndex);
            } while (arr[j] > pivot);

            if (i >= j) {
                return j;
            }

            swap(arr, i, j); 
            metric.incSwaps();
            notifyStep(arr, i, j);
        }
    }
}