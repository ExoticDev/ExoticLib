package net.exoticdev.api.tablist;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.exoticdev.api.array.GenericArray;
import net.exoticdev.api.packet.EntityWrapper;
import net.exoticdev.api.packet.EnumWrapper;
import net.exoticdev.api.packet.PacketWrapper;
import net.exoticdev.api.packet.TextWrapper;
import net.exoticdev.api.reflection.ReflectionTools;
import net.exoticdev.api.spigot.Spigot;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class Tablist {

    private TablistProperties properties;
    private Map<Integer, Map<String, Object>> slots = new HashMap<>();
    private final String DEFAULT_TEXTURE = "eyJ0aW1lc3RhbXAiOjE0MTEyNjg3OTI3NjUsInByb2ZpbGVJZCI6IjNmYmVjN2RkMGE1ZjQwYmY5ZDExODg1YTU0NTA3MTEyIiwicHJvZmlsZU5hbWUiOiJsYXN0X3VzZXJuYW1lIiwidGV4dHVyZXMiOnsiU0tJTiI6eyJ1cmwiOiJodHRwOi8vdGV4dHVyZXMubWluZWNyYWZ0Lm5ldC90ZXh0dXJlLzg0N2I1Mjc5OTg0NjUxNTRhZDZjMjM4YTFlM2MyZGQzZTMyOTY1MzUyZTNhNjRmMzZlMTZhOTQwNWFiOCJ9fX0=";
    private final String DEFAULT_SIGNATURE = "u8sG8tlbmiekrfAdQjy4nXIcCfNdnUZzXSx9BE1X5K27NiUvE1dDNIeBBSPdZzQG1kHGijuokuHPdNi/KXHZkQM7OJ4aCu5JiUoOY28uz3wZhW4D+KG3dH4ei5ww2KwvjcqVL7LFKfr/ONU5Hvi7MIIty1eKpoGDYpWj3WjnbN4ye5Zo88I2ZEkP1wBw2eDDN4P3YEDYTumQndcbXFPuRRTntoGdZq3N5EBKfDZxlw4L3pgkcSLU5rWkd5UH4ZUOHAP/VaJ04mpFLsFXzzdU4xNZ5fthCwxwVBNLtHRWO26k/qcVBzvEXtKGFJmxfLGCzXScET/OjUBak/JEkkRG2m+kpmBMgFRNtjyZgQ1w08U6HHnLTiAiio3JswPlW5v56pGWRHQT5XWSkfnrXDalxtSmPnB5LmacpIImKgL8V9wLnWvBzI7SHjlyQbbgd+kUOkLlu7+717ySDEJwsFJekfuR6N/rpcYgNZYrxDwe4w57uDPlwNL6cJPfNUHV7WEbIU1pMgxsxaXe8WSvV87qLsR7H06xocl2C0JFfe2jZR4Zh3k9xzEnfCeFKBgGb4lrOWBu1eDWYgtKV67M2Y+B3W5pjuAjwAxn0waODtEn/3jKPbc/sxbPvljUCw65X+ok0UUN1eOwXV5l2EGzn05t3Yhwq19/GxARg63ISGE8CKw=";

    public void setProperties(TablistProperties properties) {
        this.properties = properties;
    }

    public void show(Player player) {
        Scoreboard scoreboard = Spigot.getScoreboardManager().getNewScoreboard();

        if(scoreboard.equals(Spigot.getScoreboardManager().getMainScoreboard())) {
            scoreboard = Spigot.getScoreboardManager().getNewScoreboard();
        }

        TablistProperties properties = this.properties;

        if(properties != null) {
            String header = (String) properties.getProperty("header", String.class);
            String footer = (String) properties.getProperty("footer", String.class);

            PacketWrapper headerFooter = new PacketWrapper("PacketPlayOutPlayerListHeaderFooter");

            if(header != null) {
                Object component = TextWrapper.wrap("{\"text\": \"" + header + "\"}", TextWrapper.WrapMode.I_CHAT_BASE_COMPONENT);

                ReflectionTools.setFieldValue(headerFooter.getPacket(), "a", component);
            }

            if(footer != null) {
                Object component = TextWrapper.wrap("{\"text\": \"" + footer + "\"}", TextWrapper.WrapMode.I_CHAT_BASE_COMPONENT);

                ReflectionTools.setFieldValue(headerFooter.getPacket(), "b", component);
            }

            headerFooter.send(player);
        }

        EntityWrapper entityWrapper = new EntityWrapper();

        Class<?> entityArray = entityWrapper.getBlueprintClass("EntityPlayer;", "[Lnet.minecraft.server");
        Class<?> entityClass = entityWrapper.getBlueprintClass("EntityPlayer", "net.minecraft.server");

        for(Player playerEntry : Spigot.getOnlinePlayers()) {
            PacketWrapper wrapper = new PacketWrapper("PacketPlayOutPlayerInfo");

            wrapper.setParameter(0, EnumWrapper.wrap("PacketPlayOutPlayerInfo$EnumPlayerInfoAction", "REMOVE_PLAYER"));
            wrapper.setParameter(1, entityArray.cast(GenericArray.get(entityClass, entityWrapper.getHandle(playerEntry))));
            wrapper.send(player);
        }

        int entries = 80;

        if(this.properties != null) {
            if(this.properties.getProperty("size") != null) {
                entries = (int) this.properties.getProperty("size");
            }
        }

        List<Object> createdPlayers = new ArrayList<>();

        for(int i = 0; i < entries; i++) {
            Team team = scoreboard.registerNewTeam("TAB - " + this.generateTeamName(i));

            if(this.slots.get(i) == null) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), Integer.toString(i));

                profile.getProperties().put("textures", new Property("textures", this.DEFAULT_TEXTURE, this.DEFAULT_SIGNATURE));

                Object entity = entityWrapper.createEntity(profile);

                ReflectionTools.setFieldValue(entity, "listName", TextWrapper.wrap("", TextWrapper.WrapMode.CHAT_COMPONENT_TEXT));

                createdPlayers.add(entity);

                team.addEntry(profile.getName());
            } else {
                Map<String, Object> propertiesMap = this.slots.get(i);

                String line = (String) propertiesMap.get("line");

                GameProfile profile = new GameProfile(UUID.randomUUID(), Integer.toString(i));

                if(propertiesMap.get("signature") != null && propertiesMap.get("texture") != null) {
                    profile.getProperties().put("textures", new Property("textures", (String) propertiesMap.get("texture"), (String) propertiesMap.get("signature")));
                } else {
                    profile.getProperties().put("textures", new Property("textures", this.DEFAULT_TEXTURE, this.DEFAULT_SIGNATURE));
                }

                Object entity = entityWrapper.createEntity(profile);

                ReflectionTools.setFieldValue(entity, "listName", TextWrapper.wrap(line, TextWrapper.WrapMode.CHAT_COMPONENT_TEXT));

                createdPlayers.add(entity);

                team.addEntry(profile.getName());
            }
        }

        for(Object entity : createdPlayers) {
            if(entity == null) {
                continue;
            }

            PacketWrapper wrapper = new PacketWrapper("PacketPlayOutPlayerInfo");

            wrapper.setParameter(0, EnumWrapper.wrap("PacketPlayOutPlayerInfo$EnumPlayerInfoAction", "ADD_PLAYER"));
            wrapper.setParameter(1, entityArray.cast(GenericArray.get(entityClass, entity)));
            wrapper.send(player);
        }

        player.setScoreboard(scoreboard);
    }

    public void updateSlot(int slot, String name) {
        Map<String, Object> properties = new HashMap<>();

        properties.put("line", name);

        this.slots.put(slot, properties);
    }

    public void updateSlot(int slot, String name, String signature, String texture) {
        Map<String, Object> properties = new HashMap<>();

        properties.put("line", name);
        properties.put("signature", signature);
        properties.put("texture", texture);

        this.slots.put(slot, properties);
    }

    public String getSlot(int slot) {
        return (String) this.slots.get(slot).get("line");
    }

    public void removeSlot(int slot) {
        if(this.slots.get(slot) != null) {
            this.slots.remove(slot);
        }
    }

    private String generateTeamName(int index) {
        String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

        int alphabetNumbersNeeded = 1;

        if(index >= alphabet.length()) {
            for(int i = 1; i < 5; i++) {
                if(alphabet.length() * i > index) {
                    alphabetNumbersNeeded = i;

                    break;
                }
            }
        }

        if(alphabetNumbersNeeded == 1) {
            return String.valueOf(alphabet.charAt(index));
        } else {
            alphabet = StringUtils.reverse(alphabet);

            StringBuilder finalString = new StringBuilder();

            for(int i = 1; i < alphabetNumbersNeeded + 1; i++) {
                if(i == alphabetNumbersNeeded) {
                    int newI = (alphabet.length() * i) - index;

                    finalString.append(String.valueOf(alphabet.charAt(newI - 1)));
                } else {
                    finalString.append("Z");

                }
            }

            return finalString.toString();
        }
    }
}