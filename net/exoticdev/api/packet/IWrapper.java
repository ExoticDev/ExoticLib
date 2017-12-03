package net.exoticdev.api.packet;

import net.exoticdev.api.spigot.Spigot;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class IWrapper {

    public Class<?> getBlueprintClass(String clazz, String packages) {
        try {
            return Class.forName(packages + "." + Spigot.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + clazz);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object invokeMethod(Object object, String methodName, Object... parameters) {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];

        for(int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = parameters[i].getClass();
        }

        try {
            return object.getClass().getMethod(methodName, parameterTypes).invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object invokeMethod(Class<?> clazz, Object object, String methodName, Object... parameters) {
        Class<?>[] parameterTypes = new Class<?>[parameters.length];

        for(int i = 0; i < parameters.length; i++) {
            parameterTypes[i] = (Class<?>) parameters[i];
        }

        try {
            return clazz.getMethod(methodName, parameterTypes).invoke(object, parameters);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Object getMinecraftClass(String clazzName, Object... parameters) {
        try {
            Class<?> clazz = this.getBlueprintClass(clazzName, "net.minecraft.server");

            if(parameters.length == 0) {
                return clazz.newInstance();
            } else {
                Class[] parameterTypes = new Class[parameters.length];

                for(int i = 0; i < parameters.length; i++) {
                    parameterTypes[i] = parameters[i].getClass();
                }

                Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);

                constructor.setAccessible(true);

                return constructor.newInstance(parameters);
            }
        } catch (NoSuchMethodException | InstantiationException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }
    }

    public Object getMinecraftClass(int constructorIndex, String clazzName, Object... parameters) {
        try {
            Class<?> clazz = this.getBlueprintClass(clazzName, "net.minecraft.server");

            if(parameters.length == 0) {
                return clazz.newInstance();
            } else {
                int index = 0;

                for(Constructor constructor : clazz.getDeclaredConstructors()) {
                    if(index == constructorIndex) {
                        constructor.setAccessible(true);

                        return constructor.newInstance(parameters);
                    }

                    index++;
                }
            }
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            return null;
        }

        return null;
    }

    public Object getCraftClass(String craft, Object... parameters) {
        try {
            Class<?> clazz = this.getBlueprintClass(craft, "org.bukkit.craftbukkit");

            if(parameters.length == 0) {
                return clazz.newInstance();
            } else {
                Class[] parameterTypes = new Class[parameters.length];

                for(int i = 0; i < parameters.length; i++) {
                    parameterTypes[i] = parameters[i].getClass();
                }

                Constructor<?> constructor = clazz.getDeclaredConstructor(parameterTypes);

                constructor.setAccessible(true);

                return constructor.newInstance(parameters);
            }
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }
}