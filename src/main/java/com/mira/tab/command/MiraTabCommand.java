package com.mira.tab.command;

import com.mira.core.api.MiraCore;
import com.mira.tab.MiraTabPlugin;
import com.mira.tab.service.TabDisplayService;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MiraTabCommand implements CommandExecutor, TabCompleter {
    private final MiraTabPlugin plugin;
    private final MiraCore core;
    private final TabDisplayService display;

    public MiraTabCommand(MiraTabPlugin plugin, MiraCore core, TabDisplayService display) {
        this.plugin = plugin;
        this.core = core;
        this.display = display;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
                             @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            help(sender);
            return true;
        }

        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "status" -> status(sender);
            case "refresh", "apply" -> refresh(sender);
            case "reload" -> reload(sender);
            case "test" -> test(sender);
            case "help" -> help(sender);
            default -> help(sender);
        }
        return true;
    }

    private void status(CommandSender sender) {
        core.messages().send(sender, "&dMiraTab &7- &fv" + plugin.getPluginMeta().getVersion());
        core.messages().send(sender, "&7Players: &f" + Bukkit.getOnlinePlayers().size()
                + "&7 | Refresh: &f" + plugin.refreshTicks() + " ticks");
        core.messages().send(sender, "&7Scope: &f" + plugin.displayScope()
                + "&7 | Tag source: &f" + display.tagProviderName());
        core.messages().send(sender, "&7Sorting: &f" + (plugin.getConfig().getBoolean("sorting.enabled", true) ? "enabled" : "disabled"));
    }

    private void refresh(CommandSender sender) {
        display.refreshAll();
        core.messages().send(sender, "&aRefreshed MiraTab for all online players.");
    }

    private void reload(CommandSender sender) {
        plugin.reloadPlugin();
        core.messages().send(sender, "&aMiraTab configuration reloaded and reapplied.");
    }

    private void test(CommandSender sender) {
        int passed = 0;
        int total = 8;
        if (plugin.isEnabled()) passed++;
        if (plugin.getServer().getPluginManager().isPluginEnabled("MiraCore")) passed++;
        if (plugin.getServer().getPluginManager().isPluginEnabled("LuckPerms")) passed++;
        if (new File(plugin.getDataFolder(), "config.yml").isFile()) passed++;
        if (plugin.getCommand("miratab") != null) passed++;
        if (plugin.refreshTaskRunning()) passed++;
        if (plugin.refreshTicks() >= 5L) passed++;
        if (!display.tagProviderName().isBlank()) passed++;

        String color = passed == total ? "&a" : "&c";
        core.messages().send(sender, color + "MiraTab Self-Test: " + passed + "/" + total + " passed.");
        core.messages().send(sender, "&7Tag source: &f" + display.tagProviderName()
                + "&7 | Scope: &f" + plugin.displayScope());
    }

    private void help(CommandSender sender) {
        core.messages().send(sender, "&dMiraTab Admin");
        core.messages().send(sender, "&f/mtab status &7- show MiraTab runtime state");
        core.messages().send(sender, "&f/mtab refresh &7- immediately refresh all player lists");
        core.messages().send(sender, "&f/mtab reload &7- reload config and restart the refresh task");
        core.messages().send(sender, "&f/mtab test &7- run diagnostics");
        core.messages().send(sender, "&f/mtab help &7- show this help");
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command,
                                                 @NotNull String alias, @NotNull String[] args) {
        if (args.length != 1) return List.of();
        String input = args[0].toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : List.of("status", "refresh", "reload", "test", "help")) {
            if (option.startsWith(input)) result.add(option);
        }
        return result;
    }
}
