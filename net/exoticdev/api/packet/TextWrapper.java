package net.exoticdev.api.packet;

public class TextWrapper {

    public static Object wrap(String text, WrapMode mode) {
        if(mode == WrapMode.I_CHAT_BASE_COMPONENT) {
            PacketWrapper wrapper = new PacketWrapper("IChatBaseComponent$ChatSerializer");

            return wrapper.invokeMethod(wrapper.getPacket(), "a", text);
        } else {
            PacketWrapper wrapper = new PacketWrapper("ChatComponentText", text);

            return wrapper.getPacket();
        }
    }

    public enum WrapMode {

        CHAT_COMPONENT_TEXT,
        I_CHAT_BASE_COMPONENT

    }
}