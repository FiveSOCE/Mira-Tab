package com.mira.tab.service;

import com.mira.tab.MiraTabPlugin;
import com.mira.tab.util.Text;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

public final class TabDisplayService {
    private final MiraTabPlugin plugin;
    private final LuckPerms luckPerms;
    private TagProvider tagProvider;

    public TabDisplayService(MiraTabPlugin plugin, LuckPerms luckPerms, TagProvider tagProvider) {
        this.plugin = plugin;
        this.luckPerms = luckPerms;
        this.tagProvider = tagProvider;
    }

    public void setTagProvider(TagProvider tagProvider) {
        this.tagProvider = tagProvider == null ? TagProvider.NONE : tagProvider;
    }

    public String tagProviderName() {
        return tagProvider.name();
    }

    public void refreshAll() {
        List<Player> players = new ArrayList<>(Bukkit.getOnlinePlayers());
        applySorting(players);
        for (Player player : players) refresh(player);
    }

    public void refresh(Player player) {
        if (!plugin.getConfig().getBoolean("display.enabled", true)) {
            restore(player);
            return;
        }

        player.playerListName(renderName(player));
        player.sendPlayerListHeaderAndFooter(renderLines(player, "display.header"), renderLines(player, "display.footer"));
    }

    public Component renderName(Player player) {
        TabContext context = context(player);
        String format = plugin.getConfig().getString("display.player-format", "%prefix%%player%%suffix%");
        return Text.component(replace(format, player, context));
    }

    public int groupWeight(Player player) {
        if (!plugin.getConfig().getBoolean("sorting.luckperms-group-weight", true)) return 0;
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return 0;
        Group group = luckPerms.getGroupManager().getGroup(user.getPrimaryGroup());
        return group == null ? 0 : group.getWeight().orElse(0);
    }

    public void restoreAll() {
        for (Player player : Bukkit.getOnlinePlayers()) restore(player);
    }

    private void restore(Player player) {
        player.playerListName(Component.text(player.getName()));
        player.sendPlayerListHeaderAndFooter(Component.empty(), Component.empty());
        player.setPlayerListOrder(0);
    }

    private void applySorting(List<Player> players) {
        if (!plugin.getConfig().getBoolean("sorting.enabled", true)) {
            for (Player player : players) player.setPlayerListOrder(0);
            return;
        }

        players.sort(Comparator
                .comparingInt(this::groupWeight)
                .reversed()
                .thenComparing(Player::getName, String.CASE_INSENSITIVE_ORDER));

        for (int i = 0; i < players.size(); i++) {
            players.get(i).setPlayerListOrder(i);
        }
    }

    private Component renderLines(Player viewer, String path) {
        List<String> lines = plugin.getConfig().getStringList(path);
        if (lines.isEmpty()) return Component.empty();
        TabContext context = context(viewer);
        List<String> rendered = new ArrayList<>(lines.size());
        for (String line : lines) rendered.add(replace(line, viewer, context));
        return Text.component(String.join("\n", rendered));
    }

    private TabContext context(Player player) {
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        String prefix = "";
        String luckPermsSuffix = "";
        String group = "";

        if (user != null) {
            CachedMetaData meta = user.getCachedData().getMetaData();
            prefix = Optional.ofNullable(meta.getPrefix()).orElse("");
            luckPermsSuffix = Optional.ofNullable(meta.getSuffix()).orElse("");
            group = user.getPrimaryGroup();
        }

        String suffix = tagProvider.suffix(player).orElse(luckPermsSuffix);
        return new TabContext(prefix, suffix, group);
    }

    private String replace(String input, Player player, TabContext context) {
        String result = input == null ? "" : input;
        result = result.replace("%prefix%", context.prefix());
        result = result.replace("%suffix%", context.suffix());
        result = result.replace("%player%", player.getName());
        result = result.replace("%group%", context.group());
        result = result.replace("%ping%", Integer.toString(player.getPing()));
        result = result.replace("%world%", player.getWorld().getName());
        result = result.replace("%online%", Integer.toString(onlineCount(player)));
        result = result.replace("%max_players%", Integer.toString(Bukkit.getMaxPlayers()));
        return result;
    }

    private int onlineCount(Player viewer) {
        String scope = plugin.getConfig().getString("display.scope", "GLOBAL").toUpperCase(Locale.ROOT);
        return scope.equals("WORLD") ? viewer.getWorld().getPlayers().size() : Bukkit.getOnlinePlayers().size();
    }

    private record TabContext(String prefix, String suffix, String group) {
    }
}
