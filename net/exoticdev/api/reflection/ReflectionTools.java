package net.exoticdev.api.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionTools {

    public static Object getAnnotation(Method method, Class<? extends Annotation> type) {
        return method.getAnnotation(type);
    }

    public static Object getAnnotation(Class clazz, Class<? extends Annotation> type) {
        return clazz.getAnnotation(type);
    }


    public static Object getFieldValue(Object clazz, String fieldName) {
        try {
            Field field;

            boolean isPrivate = false;

            try {
                field = clazz.getClass().getField(fieldName);
            } catch (Exception e) {
                field = clazz.getClass().getDeclaredField(fieldName);

                isPrivate = true;
            }

            if(isPrivate) {
                field.setAccessible(true);
            }

            return field.get(clazz);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object getFieldValue(Class<?> clazz, Object object, String fieldName) {
        try {
            Field field;

            boolean isPrivate = false;

            try {
                field = clazz.getField(fieldName);
            } catch (Exception e) {
                field = clazz.getDeclaredField(fieldName);

                isPrivate = true;
            }

            if(isPrivate) {
                field.setAccessible(true);
            }

            return field.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void setFieldValue(Object clazz, String fieldName, Object value) {
        try {
            Field field;

            boolean isPrivate = false;

            try {
                field = clazz.getClass().getField(fieldName);
            } catch (Exception e) {
                field = clazz.getClass().getDeclaredField(fieldName);

                isPrivate = true;
            }

            if(isPrivate) {
                field.setAccessible(true);
            }

            field.set(clazz, value);

            if(isPrivate) {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void setFieldValue(Class<?> clazz, Object object, String fieldName, Object value) {
        try {
            Field field;

            boolean isPrivate = false;

            try {
                field = clazz.getField(fieldName);
            } catch (Exception e) {
                field = clazz.getDeclaredField(fieldName);

                isPrivate = true;
            }

            if(isPrivate) {
                field.setAccessible(true);
            }

            field.set(object, value);

            if(isPrivate) {
                field.setAccessible(false);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static Object invokeMethod(Object object, String methodName, Object... parameters) {
        Class[] parameterTypes = new Class[parameters.length];

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
}