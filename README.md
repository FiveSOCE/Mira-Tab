# MiraTab

MiraTab is the player-list formatting module for the Mira Minecraft plugin ecosystem. It targets **Paper 1.21.11** and **Java 21**.

## Download

[**Download MiraTab v0.1.0**](https://github.com/FiveSOCE/Mira-Tab/releases/download/v0.1.0/MiraTab-0.1.0.jar)

Current release: **v0.1.0**

## Dependencies

Hard dependencies:

- MiraCore 0.1.0+
- LuckPerms

Optional integration:

- MiraTags 0.1.3+

MiraTab reads LuckPerms prefixes directly. When MiraTags is installed, the active tag is read from the MiraTags API. If MiraTags is unavailable, MiraTab falls back to LuckPerms suffix metadata.

Vault is not required by MiraTab because LuckPerms is consumed directly.

## Default player row

```text
[prefix] Username [tag]
```

The exact spacing and formatting are controlled by the configured LuckPerms prefix/tag suffix values and `display.player-format`.

Default:

```yaml
display:
  player-format: "%prefix%%player%%suffix%"
```

## Header and footer

The header and footer are configured as lists of legacy-color-code strings:

```yaml
display:
  header:
    - "&5&lMira"
    - "&7Welcome, &f%player%"
  footer:
    - "&7Online: &f%online%&7/&f%max_players% &8| &7Ping: &f%ping%ms"
    - "&8%world%"
```

Supported placeholders:

- `%prefix%`
- `%suffix%`
- `%player%`
- `%group%`
- `%ping%`
- `%world%`
- `%online%`
- `%max_players%`

## Display scope

```yaml
display:
  scope: GLOBAL
```

`GLOBAL` makes `%online%` report the whole server. `WORLD` makes `%online%` report players in the viewer's current world.

The actual player-list roster stays global. MiraTab does not hide players from each other to simulate per-world tabs because player visibility should not be altered by a formatting plugin.

## Sorting

MiraTab sorts the player list using LuckPerms primary-group weights by default:

```yaml
sorting:
  enabled: true
  luckperms-group-weight: true
```

Higher group weight appears first. Players with the same weight are ordered alphabetically by username.

This keeps the tab hierarchy attached to the existing LuckPerms rank hierarchy instead of maintaining a second rank-order list.

## Refreshing

Default refresh interval:

```yaml
display:
  refresh-ticks: 20
```

The value is clamped between 5 and 1200 ticks. Join, quit and world-change events also trigger an immediate refresh.

## Commands

```text
/mtab status
/mtab refresh
/mtab reload
/mtab test
/mtab help
```

`/miratab` is the full command. `/mtab` is the short alias.

Permission:

```text
miratab.admin
```

Default: OP.

## MiraCore integration

MiraTab registers the `MiraTabApi` in MiraCore and reports module health through `/miracore status`.

The API supports:

- refresh one player
- refresh all players
- render a player's current tab name
- read the player's LuckPerms primary-group weight

## First test pass

1. Install MiraCore, LuckPerms and MiraTab. Keep MiraTags installed if you want direct tag integration.
2. Ensure only one MiraTab JAR exists in `/plugins`.
3. Restart Paper 1.21.11.
4. Run `/version MiraTab` and confirm `0.1.0`.
5. Run `/mtab test` and expect `8/8 passed`.
6. Run `/miracore status` and confirm MiraTab is HEALTHY.
7. Give two players different LuckPerms prefixes/group weights and confirm the higher-weight rank appears first.
8. Equip a MiraTag and confirm it appears after the username.
9. Change a prefix/tag while online and confirm the tab refreshes within the configured refresh interval.
10. Edit header/footer text, run `/mtab reload`, and confirm the change applies immediately.
11. Switch `display.scope` to `WORLD`, reload, and confirm `%online%` follows the viewer's world count.

## Building

```bash
gradle clean test build
```

Output:

```text
build/libs/MiraTab-0.1.0.jar
```
