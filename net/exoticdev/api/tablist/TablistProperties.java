package net.exoticdev.api.tablist;

import java.util.HashMap;
import java.util.Map;

public class TablistProperties {

    private Map<String, Object> properties = new HashMap<>();

    public void setProperty(String property, Object object) {
        this.properties.put(property, object);
    }

    public Object getProperty(String property) {
        return this.properties.get(property);
    }

    public Object getProperty(String property, Class<?> clazz) {
        Object object = this.getProperty(property);

        try {
            return clazz.cast(object);
        } catch (ClassCastException e) {
            return object;
        }
    }

    public Map<String, Object> getProperties() {
        return this.properties;
    }
}