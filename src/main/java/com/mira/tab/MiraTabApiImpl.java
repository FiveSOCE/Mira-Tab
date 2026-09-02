package com.mira.tab;

import com.mira.tab.api.MiraTabApi;
import com.mira.tab.service.TabDisplayService;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public final class MiraTabApiImpl implements MiraTabApi {
    private final TabDisplayService display;

    public MiraTabApiImpl(TabDisplayService display) {
        this.display = display;
    }

    @Override
    public void refresh(Player player) {
        display.refresh(player);
    }

    @Override
    public void refreshAll() {
        display.refreshAll();
    }

    @Override
    public Component renderName(Player player) {
        return display.renderName(player);
    }

    @Override
    public int groupWeight(Player player) {
        return display.groupWeight(player);
    }
}
