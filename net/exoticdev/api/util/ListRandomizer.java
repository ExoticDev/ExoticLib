package net.exoticdev.api.util;

import java.util.List;
import java.util.Random;

public class ListRandomizer {

    public static Object randomize(List<?> list) {
        return list.get(new Random().nextInt(list.size()));
    }
}