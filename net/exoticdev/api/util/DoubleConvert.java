package net.exoticdev.api.util;

import java.text.DecimalFormat;

public class DoubleConvert {

    public static double convertDouble(double var, int digits) {
        String pattern = "#,##0";

        if(digits != 0) {
            pattern = "#,##0.";
        }

        DecimalFormat oneDigit = new DecimalFormat(pattern + DoubleConvert.getMultipliedString("0", digits));

        return Double.valueOf(oneDigit.format(var).replace(",", "."));
    }

    private static String getMultipliedString(String var, int times) {
        if(times == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();

        for(int i = 0; i < times; i++) {
            builder.append(var);
        }

        return builder.toString();
    }
}