package com.kandreyss.utils;

import java.util.concurrent.ThreadLocalRandom;

public class Shuffler {
    public static int[] randomArray(int n) {
        if (n <= 0) return new int[0];

        int[] array = new int[n];
        for (int i = 0; i < n; i++) array[i] = i + 1;

        ThreadLocalRandom rand = ThreadLocalRandom.current();
        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
        }
        return array;
    }

    public static int[] descendingArray(int n) {
        if (n <= 0) return new int[0];

        int[] array = new int[n];
        for (int i = 0; i < n; i++) {
            array[i] = n - i;
        }
        return array;
    }

    public static int[] ascendingArray(int n) {
        if (n <= 0) return new int[0];

        int[] array = new int[n];
        for (int i = 0; i < n; i++) array[i] = i + 1;
        return array;
    }
}