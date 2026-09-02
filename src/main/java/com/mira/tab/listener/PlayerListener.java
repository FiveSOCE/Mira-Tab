package com.mira.tab.listener;

import com.mira.tab.MiraTabPlugin;
import com.mira.tab.service.TabDisplayService;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public final class PlayerListener implements Listener {
    private final MiraTabPlugin plugin;
    private final TabDisplayService display;

    public PlayerListener(MiraTabPlugin plugin, TabDisplayService display) {
        this.plugin = plugin;
        this.display = display;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getScheduler().runTask(plugin, display::refreshAll);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Bukkit.getScheduler().runTask(plugin, display::refreshAll);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Bukkit.getScheduler().runTask(plugin, display::refreshAll);
    }
}
