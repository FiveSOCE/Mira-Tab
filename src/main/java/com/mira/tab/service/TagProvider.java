package com.mira.tab.service;

import org.bukkit.entity.Player;

import java.util.Optional;

public interface TagProvider {
    TagProvider NONE = new TagProvider() {
        @Override
        public Optional<String> suffix(Player player) {
            return Optional.empty();
        }

        @Override
        public String name() {
            return "LuckPerms fallback";
        }
    };

    Optional<String> suffix(Player player);

    String name();
}
