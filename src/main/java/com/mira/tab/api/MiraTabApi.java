package com.mira.tab.api;

import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public interface MiraTabApi {
    void refresh(Player player);

    void refreshAll();

    Component renderName(Player player);

    int groupWeight(Player player);
}
