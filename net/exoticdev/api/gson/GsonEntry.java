package net.exoticdev.api.gson;

import com.google.gson.JsonObject;

public class GsonEntry {

    private Object finalObject;
    private JsonObject jsonObject;

    public GsonEntry(Object finalObject, JsonObject jsonObject) {
        this.finalObject = finalObject;
        this.jsonObject = jsonObject;
    }

    public Object getFinalObject() {
        return this.finalObject;
    }

    public JsonObject getJsonObject() {
        return this.jsonObject;
    }
}