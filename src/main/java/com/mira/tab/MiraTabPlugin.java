package com.mira.tab;

import com.mira.core.api.MiraCore;
import com.mira.core.api.MiraCoreProvider;
import com.mira.core.api.ModuleHealth;
import com.mira.tab.api.MiraTabApi;
import com.mira.tab.command.MiraTabCommand;
import com.mira.tab.listener.PlayerListener;
import com.mira.tab.service.MiraTagsProvider;
import com.mira.tab.service.TabDisplayService;
import com.mira.tab.service.TagProvider;
import com.mira.tab.util.TabNumbers;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.List;
import java.util.Locale;

public final class MiraTabPlugin extends JavaPlugin {
    private static final List<String> V010_HEADER = List.of(
            "&5&lMira",
            "&7Welcome, &f%player%"
    );
    private static final List<String> V010_FOOTER = List.of(
            "&7Online: &f%online%&7/&f%max_players% &8| &7Ping: &f%ping%ms",
            "&8%world%"
    );
    private static final List<String> FACTIONS_HEADER = List.of(
            "&5&l★ MIRA FACTIONS ★",
            "&7play.mira.gg",
            ""
    );
    private static final List<String> FACTIONS_FOOTER = List.of(
            "",
            "&f%online%&8/&f%max_players% &7Players Online",
            "&5mira.gg"
    );

    private MiraCore core;
    private LuckPerms luckPerms;
    private TabDisplayService display;
    private MiraTabApi api;
    private BukkitTask refreshTask;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        migrateStockV010Layout();

        core = MiraCoreProvider.require();
        luckPerms = LuckPermsProvider.get();
        display = new TabDisplayService(this, luckPerms, resolveTagProvider());
        api = new MiraTabApiImpl(display);

        core.modules().register(this, "MiraTab");
        core.services().register(MiraTabApi.class, api);

        getServer().getPluginManager().registerEvents(new PlayerListener(this, display), this);

        PluginCommand command = getCommand("miratab");
        if (command == null) {
            core.modules().setHealth(this, ModuleHealth.UNHEALTHY, "MiraTab command missing from plugin.yml");
            throw new IllegalStateException("MiraTab command missing from plugin.yml");
        }

        MiraTabCommand admin = new MiraTabCommand(this, core, display);
        command.setExecutor(admin);
        command.setTabCompleter(admin);

        startRefreshTask();
        Bukkit.getScheduler().runTask(this, display::refreshAll);

        core.modules().setHealth(this, ModuleHealth.HEALTHY,
                "Player list formatting, LuckPerms sorting and header/footer refresh ready");
        getLogger().info("MiraTab v" + getPluginMeta().getVersion() + " enabled. Tag source: "
                + display.tagProviderName() + ".");
    }

    @Override
    public void onDisable() {
        if (refreshTask != null) refreshTask.cancel();
        if (display != null) display.restoreAll();
        if (core != null) {
            if (api != null) core.services().unregister(MiraTabApi.class, api);
            core.modules().unregister(this);
        }
    }

    public void reloadPlugin() {
        reloadConfig();
        display.setTagProvider(resolveTagProvider());
        startRefreshTask();
        display.refreshAll();
    }

    public long refreshTicks() {
        return TabNumbers.refreshTicks(getConfig().getInt("display.refresh-ticks", 20));
    }

    public String displayScope() {
        String value = getConfig().getString("display.scope", "GLOBAL").toUpperCase(Locale.ROOT);
        return value.equals("WORLD") ? "WORLD" : "GLOBAL";
    }

    public boolean refreshTaskRunning() {
        return refreshTask != null && !refreshTask.isCancelled();
    }

    private void migrateStockV010Layout() {
        if (!getConfig().getString("display.player-format", "%prefix%%player%%suffix%")
                .equals("%prefix%%player%%suffix%")) return;
        if (!getConfig().getStringList("display.header").equals(V010_HEADER)) return;
        if (!getConfig().getStringList("display.footer").equals(V010_FOOTER)) return;

        getConfig().set("display.header", FACTIONS_HEADER);
        getConfig().set("display.footer", FACTIONS_FOOTER);
        saveConfig();
        getLogger().info("Upgraded the untouched MiraTab v0.1.0 layout to the factions-style default.");
    }

    private void startRefreshTask() {
        if (refreshTask != null) refreshTask.cancel();
        refreshTask = Bukkit.getScheduler().runTaskTimer(this, display::refreshAll, 1L, refreshTicks());
    }

    private TagProvider resolveTagProvider() {
        if (!getServer().getPluginManager().isPluginEnabled("MiraTags")) return TagProvider.NONE;
        try {
            return new MiraTagsProvider(core);
        } catch (Throwable error) {
            getLogger().warning("MiraTags is installed but its API could not be attached. Falling back to LuckPerms suffix metadata: "
                    + error.getMessage());
            return TagProvider.NONE;
        }
    }
}
