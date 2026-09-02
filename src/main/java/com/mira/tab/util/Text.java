package com.mira.tab.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public final class Text {
    private static final LegacyComponentSerializer AMP = LegacyComponentSerializer.legacyAmpersand();

    private Text() {
    }

    public static Component component(String input) {
        String value = input == null ? "" : input.replace('§', '&');
        return AMP.deserialize(value);
    }
}
