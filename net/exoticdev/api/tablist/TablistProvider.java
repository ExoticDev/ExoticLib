package net.exoticdev.api.tablist;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class TablistProvider extends Tablist {

    private Plugin plugin;
    private Tablist tablist;
    private Map<UUID, BukkitTask> tasks = new HashMap<>();

    public TablistProvider(Plugin plugin) {
        Tablist tablist = new Tablist();

        this.tablist = tablist;
        this.plugin = plugin;
    }

    public Tablist getHandle() {
        return this.tablist;
    }

    public void query(Player player, TablistAction action) {
        if(action == TablistAction.SHOW) {
            this.onInitialization(this.tablist, player);
            this.tablist.show(player);
        } else if(action == TablistAction.STOP_UPDATER) {
            if(this.tasks.get(player.getUniqueId()) != null) {
                this.tasks.get(player.getUniqueId()).cancel();
            }
        } else if(action == TablistAction.START_UPDATER) {
            this.tasks.put(player.getUniqueId(), new BukkitRunnable() {

                @Override
                public void run() {
                    TablistProvider.this.onUpdate(TablistProvider.this.tablist, player);
                }
            }.runTaskTimer(this.plugin, this.getRefreshTime(this.tablist, player), this.getRefreshTime(this.tablist, player)));
        }
    }

    public abstract long getRefreshTime(Tablist tablist, Player player);

    public abstract void onUpdate(Tablist tablist, Player player);

    public abstract void onInitialization(Tablist tablist, Player player);

    public enum TablistAction {

        SHOW,
        STOP_UPDATER,
        START_UPDATER

    }
}