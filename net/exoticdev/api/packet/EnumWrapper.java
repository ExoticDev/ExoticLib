package net.exoticdev.api.packet;

public class EnumWrapper {

    public static Object wrap(String packet, String enumValue) {
        IWrapper wrapper = new IWrapper();

        Class<?> clazz = wrapper.getBlueprintClass(packet, "net.minecraft.server");

        try {
            return clazz.getField(enumValue).get(null);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return null;
    }
}