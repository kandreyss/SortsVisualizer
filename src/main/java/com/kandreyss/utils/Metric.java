package com.kandreyss.utils;

public class Metric {
    private volatile long compairs = 0L;
    private volatile long swaps = 0L;

    public void incCompairs() { compairs++; }
    public void incSwaps()    { swaps++; }

    public long getCompairs() { return compairs; }
    public long getSwaps()    { return swaps; }

    public void clear() { compairs = 0L; swaps = 0L; }

    public void print() {
        System.out.println("Comparisons: " + getCompairs());
        System.out.println("Swaps:       " + getSwaps());
    }
}