package com.texthandling;

/**
 * Created by Ильнар on 21.04.2015.
 */
public class Utils {

    public static long pow(int a, int b) {
        if (b == -1) {
            return -1;
        }
        return b == 0 ? 1 : a * pow(a, b - 1);
    }
}
