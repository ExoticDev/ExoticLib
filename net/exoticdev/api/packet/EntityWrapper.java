package net.exoticdev.api.packet;

import com.mojang.authlib.GameProfile;
import net.exoticdev.api.reflection.ReflectionTools;
import net.exoticdev.api.spigot.Spigot;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class EntityWrapper extends IWrapper {

    public Object getHandle(Player player) {
        Object handle = ReflectionTools.invokeMethod(player, "getHandle");

        return handle;
    }

    public Object getConnection(Player player) {
        return ReflectionTools.getFieldValue(this.getHandle(player), "playerConnection");
    }

    public int getPing(Player player) {
        return (int) ReflectionTools.getFieldValue(this.getHandle(player), "ping");
    }

    public Object createEntity(GameProfile profile) {
        Object craftServer = this.getBlueprintClass("CraftServer", "org.bukkit.craftbukkit").cast(Spigot.getServer());
        Object craftWorld = this.getBlueprintClass("CraftWorld", "org.bukkit.craftbukkit").cast(Spigot.getWorlds().get(0));

        Object server = this.invokeMethod(craftServer, "getServer");
        Object world = this.invokeMethod(craftWorld, "getHandle");

        Object playerInteractManager = this.getMinecraftClass(0, "PlayerInteractManager", world);
        Object entity = this.getMinecraftClass(0, "EntityPlayer", server, world, profile, playerInteractManager);

        return entity;
    }

    public void sendPacket(Player player, Object object) {
        try {
            Method method = this.getConnection(player).getClass().getMethod("sendPacket", this.getBlueprintClass("Packet", "net.minecraft.server"));

            method.invoke(this.getConnection(player), object);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}