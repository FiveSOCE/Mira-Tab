package com.mira.tab.util;

public final class TabNumbers {
    private TabNumbers() {
    }

    public static long refreshTicks(int configured) {
        return Math.max(5L, Math.min(1200L, configured));
    }
}
