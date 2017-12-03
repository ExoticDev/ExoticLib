package net.exoticdev.api.gson.serializer;

import net.exoticdev.api.gson.GsonFactory;
import net.exoticdev.api.spigot.Spigot;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Serializers {


    private static boolean setup = false;
    private static List<Serializer> serializers = new ArrayList<>();

    public static void trySetup() {
        if(!setup) {
            serializers.add(new LocationSerializer());
            serializers.add(new ArraySerializer());
            serializers.add(new ListSerializer());
            serializers.add(new ItemStackSerializer());
        }
    }

    public static List<Serializer> getSerializers() {
        return serializers;
    }

    public static class LocationSerializer extends Serializer<Location> {

        @Override
        public Map<String, Map<String, Object>> serializingObject(Object object, String path, GsonFactory factory) {
            Location location = (Location) object;

            Map<String, Map<String, Object>> finalList = new HashMap<>();

            Map<String, Object> values = new HashMap<>();

            values.put("world", location.getWorld().getName());
            values.put("x", location.getX());
            values.put("y", location.getY());
            values.put("z", location.getZ());
            values.put("yaw", location.getYaw());
            values.put("pitch", location.getPitch());

            finalList.put(path, values);

            return finalList;
        }

        @Override
        public Location deserializingObject(String path, GsonFactory factory) {
            String worldName = factory.getString(path + ".world");

            double x = factory.getDouble(path + ".x");
            double y = factory.getDouble(path + ".y");
            double z = factory.getDouble(path + ".z");
            float yaw = factory.getFloat(path + ".yaw");
            float pitch = factory.getFloat(path + ".pitch");

            return new Location(Spigot.getWorld(worldName), x, y, z, yaw, pitch);
        }
    }

    public static class ItemStackSerializer extends Serializer<ItemStack> {

        @Override
        public Map<String, Map<String, Object>> serializingObject(Object object, String path, GsonFactory factory) {
            ItemStack item = (ItemStack) object;

            Map<String, Map<String, Object>> finalList = new HashMap<>();

            Map<String, Object> values = new HashMap<>();

            values.put("type", item.getType().name());
            values.put("amount", item.getAmount());
            values.put("durability", item.getDurability());
            values.put("material-data", item.getData().getData());

            if(item.getEnchantments() != null && !item.getEnchantments().isEmpty()) {
                int index = 1;

                for(Map.Entry<Enchantment, Integer> enchantment : item.getEnchantments().entrySet()) {
                    values.put("enchantments." + index + ".name", enchantment.getKey().getName().toUpperCase());
                    values.put("enchantments." + index + ".level", enchantment.getValue());

                    index++;
                }
            }

            if(item.getItemMeta() != null) {
                ItemMeta meta = item.getItemMeta();

                if(meta.getDisplayName() != null && !meta.getDisplayName().isEmpty()) {
                    values.put("meta.name", meta.getDisplayName());
                }

                if(meta.getLore() != null && !meta.getLore().isEmpty()) {
                    int index = 1;

                    for(String lore : meta.getLore()) {
                        values.put("meta.lore." + index, lore);

                        index++;
                    }
                }

                if(meta.getItemFlags() != null && !meta.getItemFlags().isEmpty()) {
                    int index = 1;

                    for(ItemFlag flag : meta.getItemFlags()) {
                        values.put("meta.itemflag." + index, flag.name());

                        index++;
                    }
                }
            }

            finalList.put(path, values);

            return finalList;
        }

        @Override
        public Object deserializingObject(String path, GsonFactory factory) {
            ItemStack item = new ItemStack(Material.valueOf(factory.getString(path + ".type").toUpperCase()));

            item.setAmount(factory.getInteger(path + ".amount"));
            item.setDurability((short) factory.getInteger(path + ".durability"));
            item.getData().setData((byte) factory.getInteger(path + ".material-data"));

            ItemMeta meta = item.getItemMeta();

            if(factory.contains(path + ".meta.name")) {
                meta.setDisplayName(factory.getString(path + ".meta.name"));
            }

            List<String> lore = new ArrayList<>();

            for(int i = 1; i < Integer.MAX_VALUE; i++) {
                if(!factory.contains(path + ".meta.lore." + i)) {
                    break;
                } else {
                    lore.add(factory.getString(path + ".meta.lore." + i));
                }
            }

            if(!lore.isEmpty()) {
                meta.setLore(lore);
            }

            for(int i = 1; i < Integer.MAX_VALUE; i++) {
                if(!factory.contains(path + ".meta.itemflag." + i)) {
                    break;
                } else {
                    meta.addItemFlags(ItemFlag.valueOf(factory.getString(path + ".meta.itemflag." + i)));
                }
            }

            for(int i = 1; i < Integer.MAX_VALUE; i++) {
                String enchantPath = path + ".enchantments." + i;

                if(!factory.contains(enchantPath)) {
                    break;
                } else {
                    meta.addEnchant(Enchantment.getByName(factory.getString(enchantPath + ".name")), factory.getInteger(enchantPath + ".level"), true);
                }
            }

            item.setItemMeta(meta);

            return item;
        }
    }

    public static class ArraySerializer extends Serializer<Object[]> {

        @Override
        public Map<String, Map<String, Object>> serializingObject(Object object, String path, GsonFactory factory) {
            String[] array = (String[]) object;

            Map<String, Map<String, Object>> finalList = new HashMap<>();

            Map<String, Object> values = new HashMap<>();

            StringBuilder builder = new StringBuilder();

            boolean first = true;

            for(String arrayEntry : array) {
                if(arrayEntry.isEmpty()) {
                    continue;
                }

                if(first) {
                    builder.append(arrayEntry);
                } else {
                    builder.append(", ").append(arrayEntry);
                }

                first = false;
            }

            values.put(path, builder.toString());

            finalList.put("", values);

            return finalList;
        }

        @Override
        public Object deserializingObject(String path, GsonFactory factory) {
            String arrayString = factory.getString(path).replace(" ", "");

            return arrayString.split(",");
        }
    }

    public static class ListSerializer extends Serializer<List> {

        @Override
        public Map<String, Map<String, Object>> serializingObject(Object object, String path, GsonFactory factory) {
            List<String> array = (List<String>) object;

            Map<String, Map<String, Object>> finalList = new HashMap<>();

            Map<String, Object> values = new HashMap<>();

            StringBuilder builder = new StringBuilder();

            boolean first = true;

            for(String arrayEntry : array) {
                if(arrayEntry.isEmpty()) {
                    continue;
                }

                if(first) {
                    builder.append(arrayEntry);
                } else {
                    builder.append(", ").append(arrayEntry);
                }

                first = false;
            }

            values.put(path, builder.toString());

            finalList.put("", values);

            return finalList;
        }

        @Override
        public Object deserializingObject(String path, GsonFactory factory) {
            String arrayString = factory.getString(path).replace(" ", "");

            String[] arrays = arrayString.split(",");

            return Arrays.asList(arrays);
        }
    }
}