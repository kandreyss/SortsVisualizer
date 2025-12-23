package com.kandreyss.sorts;

public interface SortListener {
    void onStep(int[] array, int index1, int index2, long compairs, long swaps);
}