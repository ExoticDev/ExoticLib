package net.exoticdev.api.gson.serializer;

import net.exoticdev.api.gson.GsonFactory;
import net.exoticdev.api.gson.serializer.exception.IllegalObjectDeserialization;
import net.exoticdev.api.gson.serializer.exception.IllegalObjectSerialization;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

/**
 * Created by ExoticDev on 2017-10-26
 */

public abstract class Serializer<T> {

    private Class<T> getSerializingClass() {
        return (Class<T>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public Map<String, Map<String, Object>> callSerialization(Object object, String path, GsonFactory factory) throws IllegalObjectSerialization {
        if(this.getSerializingClass().isInstance(object)) {
            return this.serializingObject(object, path, factory);
        } else {
            throw new IllegalObjectSerialization("Tried serializing a non '" + this.getSerializingClass().toString() + "' object - " + object.getClass().toString());
        }
    }

    public Object callDeserialization(String path, GsonFactory factory, Class<?> clazz) throws IllegalObjectDeserialization {
        if(this.getSerializingClass().getName().equals(clazz.getName())) {
            return this.deserializingObject(path, factory);
        } else {
            throw new IllegalObjectDeserialization("Tried deserializing a non '" + this.getSerializingClass().toString() + "' class - " + clazz.getName());
        }
    }

    public abstract Map<String, Map<String, Object>> serializingObject(Object object, String path, GsonFactory factory);

    public abstract Object deserializingObject(String path, GsonFactory factory);

}