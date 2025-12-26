package com.kandreyss.sorts.smart;

import com.kandreyss.sorts.Sort;

public class HeapSort extends Sort {
    public HeapSort(int[] array) {
        super("Heap Sort", array, 1);
    }
    
    @Override
    public int[] sort() {
        int[] tmp = array.clone();
        heapsort(tmp, tmp.length);

        notifyStep(tmp, -1, -1);
        return tmp;
    };

    private void heapify(int arr[], int n, int i) {
        int largest = i;
        int L = 2 * i + 1;
        int R = 2 * i + 2;

        metric.incCompairs();
        notifyStep(arr, L, largest);
        if(L < n && arr[L] > arr[largest])
            largest = L;

        metric.incCompairs();
        notifyStep(arr, R, largest);
        if(R < n && arr[R] > arr[largest]) 
            largest = R;

        if(largest != i) {
            metric.incSwaps();
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }

    private void heapsort(int arr[], int n) {
        for(int i = n/2 - 1; i >= 0; i--) {  
            heapify(arr, n, i);
        }

        for(int i = n - 1; i > 0; i--) {
            metric.incSwaps();
            swap(arr, i, 0);
            heapify(arr, i, 0);
        }
    }
}
