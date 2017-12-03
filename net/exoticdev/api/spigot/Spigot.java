package net.exoticdev.api.spigot;

import com.avaje.ebean.config.ServerConfig;
import org.bukkit.*;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.help.HelpMap;
import org.bukkit.inventory.*;
import org.bukkit.map.MapView;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.CachedServerIcon;
import org.spigotmc.CustomTimingsHandler;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;
import java.util.logging.Logger;

public final class Spigot {

    private static Server server = Bukkit.getServer();

    public static Server getServer() {
        return Spigot.server;
    }

    public static void setServer(final Server server) {
        if(Spigot.server != null) {
            throw new UnsupportedOperationException("Cannot redefine singleton Server");
        }

        Spigot.server = server;
    }

    public static String getName() {
        return Spigot.server.getName();
    }

    public static String getVersion() {
        return Spigot.server.getVersion();
    }

    public static String getBukkitVersion() {
        return Spigot.server.getBukkitVersion();
    }

    public static Collection<? extends Player> getOnlinePlayers() {
        return Spigot.server.getOnlinePlayers();
    }

    public static int getMaxPlayers() {
        return Spigot.server.getMaxPlayers();
    }

    public static int getPort() {
        return Spigot.server.getPort();
    }

    public static int getViewDistance() {
        return Spigot.server.getViewDistance();
    }

    public static String getIp() {
        return Spigot.server.getIp();
    }

    public static String getServerName() {
        return Spigot.server.getServerName();
    }

    public static String getServerId() {
        return Spigot.server.getServerId();
    }

    public static String getWorldType() {
        return Spigot.server.getWorldType();
    }

    public static boolean getGenerateStructures() {
        return Spigot.server.getGenerateStructures();
    }

    public static boolean getAllowEnd() {
        return Spigot.server.getAllowEnd();
    }

    public static boolean getAllowNether() {
        return Spigot.server.getAllowNether();
    }

    public static boolean hasWhitelist() {
        return Spigot.server.hasWhitelist();
    }

    public static void setWhitelist(final boolean value) {
        Spigot.server.setWhitelist(value);
    }

    public static Set<OfflinePlayer> getWhitelistedPlayers() {
        return Spigot.server.getWhitelistedPlayers();
    }

    public static void reloadWhitelist() {
        Spigot.server.reloadWhitelist();
    }

    public static int broadcastMessage(final String message) {
        return Spigot.server.broadcastMessage(message);
    }

    public static String getUpdateFolder() {
        return Spigot.server.getUpdateFolder();
    }

    public static File getUpdateFolderFile() {
        return Spigot.server.getUpdateFolderFile();
    }

    public static long getConnectionThrottle() {
        return Spigot.server.getConnectionThrottle();
    }

    public static int getTicksPerAnimalSpawns() {
        return Spigot.server.getTicksPerAnimalSpawns();
    }

    public static int getTicksPerMonsterSpawns() {
        return Spigot.server.getTicksPerMonsterSpawns();
    }

    public static Player getPlayer(final String name) {
        return Spigot.server.getPlayer(name);
    }

    public static Player getPlayerExact(final String name) {
        return Spigot.server.getPlayerExact(name);
    }

    public static List<Player> matchPlayer(final String name) {
        return Spigot.server.matchPlayer(name);
    }

    public static Player getPlayer(final UUID id) {
        return Spigot.server.getPlayer(id);
    }

    public static PluginManager getPluginManager() {
        return Spigot.server.getPluginManager();
    }

    public static BukkitScheduler getScheduler() {
        return Spigot.server.getScheduler();
    }

    public static ServicesManager getServicesManager() {
        return Spigot.server.getServicesManager();
    }

    public static List<World> getWorlds() {
        return Spigot.server.getWorlds();
    }

    public static World createWorld(final WorldCreator creator) {
        return Spigot.server.createWorld(creator);
    }

    public static boolean unloadWorld(final String name, final boolean save) {
        return Spigot.server.unloadWorld(name, save);
    }

    public static boolean unloadWorld(final World world, final boolean save) {
        return Spigot.server.unloadWorld(world, save);
    }

    public static World getWorld(final String name) {
        return Spigot.server.getWorld(name);
    }

    public static World getWorld(final UUID uid) {
        return Spigot.server.getWorld(uid);
    }

    @Deprecated
    public static MapView getMap(final short id) {
        return Spigot.server.getMap(id);
    }

    public static MapView createMap(final World world) {
        return Spigot.server.createMap(world);
    }

    public static void reload() {
        Spigot.server.reload();

        CustomTimingsHandler.reload();
    }

    public static Logger getLogger() {
        return Spigot.server.getLogger();
    }

    public static PluginCommand getPluginCommand(final String name) {
        return Spigot.server.getPluginCommand(name);
    }

    public static void savePlayers() {
        Spigot.server.savePlayers();
    }

    public static boolean dispatchCommand(final CommandSender sender, final String commandLine) throws CommandException {
        return Spigot.server.dispatchCommand(sender, commandLine);
    }

    public static void configureDbConfig(final ServerConfig config) {
        Spigot.server.configureDbConfig(config);
    }

    public static boolean addRecipe(final Recipe recipe) {
        return Spigot.server.addRecipe(recipe);
    }

    public static List<Recipe> getRecipesFor(final ItemStack result) {
        return Spigot.server.getRecipesFor(result);
    }

    public static Iterator<Recipe> recipeIterator() {
        return Spigot.server.recipeIterator();
    }

    public static void clearRecipes() {
        Spigot.server.clearRecipes();
    }

    public static void resetRecipes() {
        Spigot.server.resetRecipes();
    }

    public static Map<String, String[]> getCommandAliases() {
        return Spigot.server.getCommandAliases();
    }

    public static int getSpawnRadius() {
        return Spigot.server.getSpawnRadius();
    }

    public static void setSpawnRadius(final int value) {
        Spigot.server.setSpawnRadius(value);
    }

    public static boolean getOnlineMode() {
        return Spigot.server.getOnlineMode();
    }

    public static boolean getAllowFlight() {
        return Spigot.server.getAllowFlight();
    }

    public static boolean isHardcore() {
        return Spigot.server.isHardcore();
    }

    @Deprecated
    public static boolean useExactLoginLocation() {
        return Spigot.server.useExactLoginLocation();
    }

    public static void shutdown() {
        Spigot.server.shutdown();
    }

    public static int broadcast(final String message, final String permission) {
        return Spigot.server.broadcast(message, permission);
    }

    @Deprecated
    public static OfflinePlayer getOfflinePlayer(final String name) {
        return Spigot.server.getOfflinePlayer(name);
    }

    public static OfflinePlayer getOfflinePlayer(final UUID id) {
        return Spigot.server.getOfflinePlayer(id);
    }

    public static Set<String> getIPBans() {
        return Spigot.server.getIPBans();
    }

    public static void banIP(final String address) {
        Spigot.server.banIP(address);
    }

    public static void unbanIP(final String address) {
        Spigot.server.unbanIP(address);
    }

    public static Set<OfflinePlayer> getBannedPlayers() {
        return Spigot.server.getBannedPlayers();
    }

    public static BanList getBanList(final BanList.Type type) {
        return Spigot.server.getBanList(type);
    }

    public static Set<OfflinePlayer> getOperators() {
        return Spigot.server.getOperators();
    }

    public static GameMode getDefaultGameMode() {
        return Spigot.server.getDefaultGameMode();
    }

    public static void setDefaultGameMode(final GameMode mode) {
        Spigot.server.setDefaultGameMode(mode);
    }

    public static ConsoleCommandSender getConsoleSender() {
        return Spigot.server.getConsoleSender();
    }

    public static File getWorldContainer() {
        return Spigot.server.getWorldContainer();
    }

    public static OfflinePlayer[] getOfflinePlayers() {
        return Spigot.server.getOfflinePlayers();
    }

    public static Messenger getMessenger() {
        return Spigot.server.getMessenger();
    }

    public static HelpMap getHelpMap() {
        return Spigot.server.getHelpMap();
    }

    public static Inventory createInventory(final InventoryHolder owner, final InventoryType type) {
        return Spigot.server.createInventory(owner, type);
    }

    public static Inventory createInventory(final InventoryHolder owner, final InventoryType type, final String title) {
        return Spigot.server.createInventory(owner, type, title);
    }

    public static Inventory createInventory(final InventoryHolder owner, final int size) throws IllegalArgumentException {
        return Spigot.server.createInventory(owner, size);
    }

    public static Inventory createInventory(final InventoryHolder owner, final int size, final String title) throws IllegalArgumentException {
        return Spigot.server.createInventory(owner, size, title);
    }

    public static int getMonsterSpawnLimit() {
        return Spigot.server.getMonsterSpawnLimit();
    }

    public static int getAnimalSpawnLimit() {
        return Spigot.server.getAnimalSpawnLimit();
    }

    public static int getWaterAnimalSpawnLimit() {
        return Spigot.server.getWaterAnimalSpawnLimit();
    }

    public static int getAmbientSpawnLimit() {
        return Spigot.server.getAmbientSpawnLimit();
    }

    public static boolean isPrimaryThread() {
        return Spigot.server.isPrimaryThread();
    }

    public static String getMotd() {
        return Spigot.server.getMotd();
    }

    public static String getShutdownMessage() {
        return Spigot.server.getShutdownMessage();
    }

    public static Warning.WarningState getWarningState() {
        return Spigot.server.getWarningState();
    }

    public static ItemFactory getItemFactory() {
        return Spigot.server.getItemFactory();
    }

    public static ScoreboardManager getScoreboardManager() {
        return Spigot.server.getScoreboardManager();
    }

    public static CachedServerIcon getServerIcon() {
        return Spigot.server.getServerIcon();
    }

    public static CachedServerIcon loadServerIcon(final File file) throws Exception {
        return Spigot.server.loadServerIcon(file);
    }

    public static CachedServerIcon loadServerIcon(final BufferedImage image) throws Exception {
        return Spigot.server.loadServerIcon(image);
    }

    public static void setIdleTimeout(final int threshold) {
        Spigot.server.setIdleTimeout(threshold);
    }

    public static int getIdleTimeout() {
        return Spigot.server.getIdleTimeout();
    }

    public static ChunkGenerator.ChunkData createChunkData(final World world) {
        return Spigot.server.createChunkData(world);
    }

    @Deprecated
    public static UnsafeValues getUnsafe() {
        return Spigot.server.getUnsafe();
    }

    public static Server.Spigot spigot() {
        return Spigot.server.spigot();
    }
}