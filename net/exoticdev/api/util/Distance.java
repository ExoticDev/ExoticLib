package net.exoticdev.api.util;

import org.bukkit.Location;

public class Distance {

    public static double getDistance(Location location1, Location location2, boolean useY) {
        if(useY) {
            return Math.sqrt(square(location1.getX() - location2.getX()) + square(location1.getY() - location2.getY()) + square(location1.getZ() - location2.getZ()));
        } else {
            return Math.sqrt(square(location1.getX() - location2.getX()) + square(location1.getZ() - location2.getZ()));
        }
    }

    private static double square(double var) {
        return var * var;
    }
}