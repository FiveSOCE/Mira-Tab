package com.mira.tab.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TabNumbersTest {
    @Test
    void clampsRefreshTicks() {
        assertEquals(5L, TabNumbers.refreshTicks(-1));
        assertEquals(20L, TabNumbers.refreshTicks(20));
        assertEquals(1200L, TabNumbers.refreshTicks(5000));
    }
}
