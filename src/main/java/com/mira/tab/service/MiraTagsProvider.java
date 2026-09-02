package com.mira.tab.service;

import com.mira.core.api.MiraCore;
import com.mira.tags.api.MiraTagsApi;
import org.bukkit.entity.Player;

import java.util.Optional;

public final class MiraTagsProvider implements TagProvider {
    private final MiraTagsApi api;

    public MiraTagsProvider(MiraCore core) {
        this.api = core.services().get(MiraTagsApi.class)
                .orElseThrow(() -> new IllegalStateException("MiraTags is enabled but its API is not registered in MiraCore."));
    }

    @Override
    public Optional<String> suffix(Player player) {
        return api.activeTag(player.getUniqueId())
                .flatMap(api::tag)
                .map(tag -> tag.suffix());
    }

    @Override
    public String name() {
        return "MiraTags API";
    }
}
