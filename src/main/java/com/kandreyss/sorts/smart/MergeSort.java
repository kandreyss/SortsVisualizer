package com.kandreyss.sorts.smart;

import com.kandreyss.sorts.Sort;

public class MergeSort extends Sort {
    public MergeSort(int[] array) {
        super("Merge Sort", array, 1);
    }

    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        merge_sort(tmp, 0, tmp.length - 1);

        notifyStep(tmp, -1, -1);
        return tmp;
    }

    private void merge_sort(int[] arr, int l, int r) {
        if (l < r) {
            int m = l + (r - l) / 2;
            merge_sort(arr, l, m);
            merge_sort(arr, m + 1, r);
            merge(arr, l, m, r);
        }
    }

    private void merge(int[] arr, int l, int m, int r) {
        int n1 = m - l + 1;
        int n2 = r - m;

        int[] al = new int[n1];
        int[] ar = new int[n2];

        for (int i = 0; i < n1; i++)
            al[i] = arr[l + i];
        for (int i = 0; i < n2; i++)
            ar[i] = arr[m + 1 + i];

        int i = 0, j = 0, k = l;
        while (i < n1 && j < n2) {
            metric.incCompairs();
            notifyStep(arr, i, i);
            if (al[i] <= ar[j]) {
                arr[k++] = al[i++];
                metric.incSwaps();
                notifyStep(arr, k, i);
            } else {
                arr[k++] = ar[j++];
                notifyStep(arr, k, j);
                metric.incSwaps();
            }
        }

        while (i < n1) {
            arr[k++] = al[i++];
            notifyStep(arr, k, i);
            metric.incSwaps();
        }
        while (j < n2) {
            arr[k++] = ar[j++];
            notifyStep(arr, k, j);
            metric.incSwaps();
        }
    }
}
