package net.exoticdev.api.util;

public class Difference {

    public static double getDoubleDifference(double var1, double var2) {
        if(var1 > var2) {
            return var1 - var2;
        } else {
            return var2 - var1;
        }
    }

    public static float getFloatDifference(float var1, float var2) {
        if(var1 > var2) {
            return var1 - var2;
        } else {
            return var2 - var1;
        }
    }

}