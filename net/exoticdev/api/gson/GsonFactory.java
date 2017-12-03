package net.exoticdev.api.gson;

import com.google.gson.*;
import net.exoticdev.api.gson.serializer.Serializer;
import net.exoticdev.api.gson.serializer.Serializers;
import net.exoticdev.api.gson.serializer.exception.IllegalObjectDeserialization;
import net.exoticdev.api.gson.serializer.exception.IllegalObjectSerialization;
import org.apache.commons.io.IOUtils;
import org.bukkit.Location;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

public class GsonFactory {

    private File file;
    private String json;

    public GsonFactory(File file) {
        this.file = file;

        File directories = file.getParentFile();

        if(!directories.exists()) {
            directories.mkdirs();
        }

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if(this.getFileInput(file).isEmpty()) {
            this.json = this.checkForTemplate(file.getName());
            this.updateJson(this.json);
        } else {
            this.json = this.getFileInput(file);
        }

        Serializers.trySetup();
    }

    public GsonFactory(String input) {
        this.json = input;

        Serializers.trySetup();
    }

    public boolean getBoolean(String path) {
        return Boolean.parseBoolean((String) this.getObject(path).getFinalObject());
    }

    public float getFloat(String path) {
        return Float.parseFloat((String) this.getObject(path).getFinalObject());
    }

    public double getDouble(String path) {
        return Double.parseDouble((String) this.getObject(path).getFinalObject());
    }

    public int getInteger(String path) {
        Double doubleValue = Double.parseDouble(this.getObject(path).getFinalObject().toString());

        return doubleValue.intValue();
    }

    public String getString(String path) {
        return (String) this.getObject(path).getFinalObject();
    }

    public List<String> getStringList(String path) {
        return (List<String>) this.getObject(path).getFinalObject();
    }

    public List<Object> getObjectList(String path) {
        return (List<Object>) this.getObject(path).getFinalObject();
    }

    public boolean contains(String path) {
        return this.getObject(path) != null;
    }

    public File getFile() {
        return this.file;
    }

    public Location getLocation(String path, boolean useYawAndPitch) {
        if(useYawAndPitch) {
            return (Location) this.getDeserializedObject(path, Location.class);
        } else {
            Location serializedLocation = (Location) this.getDeserializedObject(path, Location.class);

            return new Location(serializedLocation.getWorld(), serializedLocation.getX(), serializedLocation.getY(), serializedLocation.getZ());
        }
    }

    public String getFileInput(File file) {
        Scanner reader = null;

        try {
            reader = new Scanner(file);
        } catch (Exception e) {
            throw new RuntimeException("Couldn't initialize Scanner.");
        }

        StringBuilder lines = new StringBuilder();

        while (reader.hasNextLine()) {
            lines.append(reader.nextLine());
        }

        reader.close();

        return lines.toString();
    }

    public void refreshConfig() {
        if(this.file != null && this.file.exists()) {
            this.json = this.getFileInput(this.file);
        }
    }

    public void writeAndSerialize(String path, Object object) {
        int failedSerializations = 0;

        for(Serializer serializer : Serializers.getSerializers()) {
            try {
                this.writeSerializedMap(serializer.callSerialization(object, path, this));
            } catch (IllegalObjectSerialization e) {
                failedSerializations++;
            }
        }

        if(failedSerializations == Serializers.getSerializers().size()) {
            this.writeData(path, object.toString());
        }
    }

    public Object getDeserializedObject(String path, Class<?> clazz) {
        for(Serializer serializer : Serializers.getSerializers()) {
            try {
                return serializer.callDeserialization(path, this, clazz);
            } catch (IllegalObjectDeserialization e) {
            }
        }

        return "";
    }

    public void writeData(String path, Object object) {
        String[] elements = path.split("\\.");

        elements = Arrays.copyOf(elements, elements.length - 1);

        String property = path.split("\\.")[elements.length];

        Gson gson = new GsonBuilder().create();

        if(elements.length > 0) {
            this.createNonExistingElements(elements, gson);
        }

        GsonEntry entry = this.getObject(path);

        JsonObject jsonObject = null;

        if(entry == null || entry.getJsonObject() == null) {
            jsonObject = new JsonObject();
            jsonObject.add(property, new JsonPrimitive("null"));
        }

        String startingJson = this.json;

        if(jsonObject == null) {
            jsonObject = entry.getJsonObject();
        }

        JsonElement startingJsonElement = gson.fromJson(startingJson, JsonElement.class);

        JsonObject currentObject = startingJsonElement.getAsJsonObject();

        for(String element : elements) {
            if(currentObject.get(element) == null) {
                currentObject.add(element, new JsonObject());

                currentObject = currentObject.getAsJsonObject(element);
            } else {
                currentObject = currentObject.getAsJsonObject(element);
            }
        }

        if(this.getJsonPrimitive(object) == null) {
            currentObject.remove(property);
        } else {
            currentObject.add(property, this.getJsonPrimitive(object));
        }

        this.updateJson(startingJsonElement.toString());
    }

    private void writeSerializedMap(Map<String, Map<String, Object>> map) {
        for(Map.Entry<String, Map<String, Object>> mapEntry : map.entrySet()) {
            this.writeData(mapEntry.getKey(), null);

            for(Map.Entry<String, Object> mapValues : mapEntry.getValue().entrySet()) {
                this.writeData(mapEntry.getKey() + "." + mapValues.getKey(), mapValues.getValue());
            }
        }
    }

    public <T extends GsonEntry> T getObject(String path) {
        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(this.json);

        JsonObject currentObject;

        if(element.isJsonObject()) {
            currentObject = element.getAsJsonObject();

            String[] elements = path.split("\\.");

            if(elements.length <= 1) {
                if(currentObject.get(path) == null) {
                    return null;
                }

                if(currentObject.get(path) instanceof JsonPrimitive) {
                    return (T) new GsonEntry(currentObject.getAsJsonPrimitive(path).getAsString(), currentObject);
                } else if(currentObject.get(path) instanceof JsonArray) {
                    return (T) new GsonEntry(new Gson().fromJson(currentObject.getAsJsonArray(path), ArrayList.class), currentObject);
                } else {
                    return (T) new GsonEntry(currentObject.getAsJsonObject(path), currentObject);
                }
            }

            for(int i = 0; i < elements.length; i++) {
                String currentElement = elements[i];

                if((i + 1) == elements.length) {
                    if(currentObject == null || currentObject.get(currentElement) == null) {
                        return null;
                    }

                    if(currentObject.get(currentElement) instanceof JsonPrimitive) {
                        return (T) new GsonEntry(currentObject.getAsJsonPrimitive(currentElement).getAsString(), currentObject);
                    } else if(currentObject.get(currentElement) instanceof JsonArray) {
                        return (T) new GsonEntry(new Gson().fromJson(currentObject.getAsJsonArray(currentElement), ArrayList.class), currentObject);
                    } else if(currentObject.get(currentElement) instanceof JsonObject) {
                        return (T) new GsonEntry(currentObject.getAsJsonObject(currentElement), currentObject);
                    }
                } else {
                    if(currentObject.get(currentElement) == null) {
                        currentObject.add(currentElement, new JsonObject());

                        currentObject = currentObject.getAsJsonObject(currentElement);
                    } else {
                        currentObject = currentObject.getAsJsonObject(currentElement);
                    }
                }
            }
        }

        return null;
    }

    private void updateJson(String json) {
        if(this.file != null && this.file.exists()) {
            PrintStream printStream = null;

            try {
                printStream = new PrintStream(new FileOutputStream(this.file));

                JsonParser parser = new JsonParser();
                Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

                JsonElement element = parser.parse(json);

                printStream.print(gson.toJson(element));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            if(printStream != null) {
                printStream.close();
            }
        }

        this.json = json;
    }

    private String checkForTemplate(String configName) {
        if(configName == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            URL url = this.getClass().getClassLoader().getResource(configName);

            if(url == null) {
                return "{}";
            }

            URLConnection connection = url.openConnection();

            connection.setUseCaches(false);

            InputStream stream = connection.getInputStream();

            return IOUtils.toString(stream);
        } catch (IOException ex) {
            return "{}";
        }
    }

    private void createNonExistingElements(String[] elements, Gson gson) {
        String startingJson = this.json;

        gson.fromJson(startingJson, JsonElement.class).getAsJsonObject().add(elements[0], new JsonObject());
    }

    private JsonPrimitive getJsonPrimitive(Object object) {
        if(object instanceof Number) {
            return new JsonPrimitive((Number) object);
        } else if(object instanceof String) {
            return new JsonPrimitive((String) object);
        } else if(object instanceof Boolean) {
            return new JsonPrimitive((Boolean) object);
        } else if(object instanceof Character) {
            return new JsonPrimitive((Character) object);
        } else {
            if(object == null) {
                return null;
            }

            return new JsonPrimitive(object.toString());
        }
    }
}