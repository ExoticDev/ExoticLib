package net.exoticdev.api.packet;

import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PacketWrapper extends IWrapper {

    private Object packet;
    private String originalPacketName;
    private Object[] parameters;

    public PacketWrapper(String packet, Object... parameters) {
        this.originalPacketName = packet;
        this.packet = this.getMinecraftClass(packet, parameters);
        this.parameters = parameters;
    }

    public PacketWrapper(int constructorIndex, String packet, Object... parameters) {
        this.originalPacketName = packet;
        this.packet = this.getMinecraftClass(constructorIndex, packet, parameters);
        this.parameters = parameters;
    }


    public void setParameter(int index, Object object) {
        List<Object> parameters = new ArrayList<>();

        parameters.addAll(Arrays.asList(this.parameters));

        try {
            parameters.set(index, object);
        } catch (Exception e) {
            parameters.add(index, object);
        }

        this.parameters = parameters.toArray();

        Object packet = this.getMinecraftClass(this.originalPacketName, this.parameters);

        if(packet != null) {
            this.packet = packet;
        }
    }

    public void write(String fieldName, Object object, Class<?> clazz) {
        try {
            Field field = this.packet.getClass().getDeclaredField(fieldName);

            field.setAccessible(true);
            field.set(this.packet, clazz.cast(object));
            field.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void write(int index, Object object, Class<?> clazz) {
        int currentIndex = 0;

        for(Field field : this.packet.getClass().getDeclaredFields()) {
            if(currentIndex == index) {
                this.write(field.getName(), object, clazz);
            }
        }
    }

    public void send(Player player) {
        EntityWrapper wrapper = new EntityWrapper();

        wrapper.sendPacket(player, this.packet);
    }

    public Object getPacket() {
        return this.packet;
    }
}