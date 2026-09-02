# MiraTab

MiraTab is the player-list formatting module for the Mira Minecraft plugin ecosystem. It targets **Paper 1.21.11** and **Java 21**.

## Download

[**Download MiraTab v0.1.2**](https://github.com/FiveSOCE/Mira-Tab/releases/download/v0.1.2/MiraTab-0.1.2.jar)

Current release: **v0.1.2**

## Dependencies

Hard dependencies:

- MiraCore 0.1.0+
- LuckPerms

Optional integration:

- MiraTags 0.1.3+

MiraTab reads LuckPerms prefixes directly. When MiraTags is installed, the active tag is read from the MiraTags API. If MiraTags is unavailable, MiraTab falls back to LuckPerms suffix metadata.

Vault is not required by MiraTab because LuckPerms is consumed directly.

## Default factions layout

MiraTab now ships with the factions-style layout as its default:

```text
               ★ MIRA FACTIONS ★
                 play.mira.gg

[OWNER] FiveS [King]
[ADMIN] Atomic [OG]
[MOD] Kuooko
[MVP+] PlayerOne
[MVP] PlayerTwo [Grinder]
[MEMBER] Steve
[MEMBER] Alex [PvPer]

         7/100 Players Online
              mira.gg
```

Minecraft centers header/footer lines automatically and renders its normal connection indicator at the far right of each player row.

The actual player row remains:

```text
[prefix] Username [tag]
```

The exact spacing and formatting come from the LuckPerms prefix, MiraTags suffix and:

```yaml
display:
  player-format: "%prefix%%player%%suffix%"
```

## Default header and footer

```yaml
display:
  header:
    - "&5&l★ MIRA FACTIONS ★"
    - "&7play.mira.gg"
    - ""
  footer:
    - ""
    - "&f%online%&8/&f%max_players% &7Players Online"
    - "&5mira.gg"
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

## v0.1.0 upgrade handling

v0.1.2 detects the exact untouched v0.1.0 stock header/footer and upgrades it automatically to the factions-style layout.

If the existing layout was customized, MiraTab leaves it untouched.

## Display scope

```yaml
display:
  scope: GLOBAL
```

`GLOBAL` makes `%online%` report the whole server. `WORLD` makes `%online%` report players in the viewer's current world.

The actual player-list roster stays global. MiraTab does not hide players to simulate per-world tabs because a formatting plugin should not change player visibility.

## Sorting

MiraTab sorts the player list using LuckPerms primary-group weights by default:

```yaml
sorting:
  enabled: true
  luckperms-group-weight: true
```

Higher group weight appears first. Players with the same weight are ordered alphabetically by username.

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

The API supports refreshing one/all players, rendering a player's current tab name, and reading LuckPerms primary-group weight.

## First test pass

1. Replace the old MiraTab JAR and ensure only one MiraTab JAR exists in `/plugins`.
2. Restart Paper 1.21.11.
3. Run `/version MiraTab` and confirm `0.1.2`.
4. Run `/mtab test` and expect `8/8 passed`.
5. Press TAB and confirm the factions header/footer appears.
6. Equip a MiraTag and confirm it appears after the username.
7. Confirm LuckPerms group weights control player ordering.
8. If upgrading an untouched v0.1.0 config, confirm it migrated automatically.

## Building

```bash
gradle clean test build
```

Output:

```text
build/libs/MiraTab-0.1.2.jar
```
