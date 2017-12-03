package net.exoticdev.api.array;

import java.lang.reflect.Array;

public class GenericArray {

    public static <Type> Type[] get(Class<Type> type, Object... object) {
        Type[] array = (Type[]) Array.newInstance(type, object.length);

        for(int i = 0; i < object.length; i++) {
            array[i] = (Type) object[i];
        }

        return array;
    }
}