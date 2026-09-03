# MiraTab

MiraTab is the player-list formatting module for the Mira Paper server suite. It builds a factions-style TAB list using LuckPerms ranks, optional MiraTags data, configurable headers/footers and group-weight sorting.

## Download

[**Download MiraTab v0.1.2**](https://github.com/FiveSOCE/Mira-Tab/releases/download/v0.1.2/MiraTab-0.1.2.jar)

## Requirements / Dependencies

- Paper 1.21.11
- Java 21
- MiraCore 0.1.0 or newer
- LuckPerms
- MiraTags 0.1.3+ optional

## How MiraTab Works

MiraTab reads each player's LuckPerms prefix and primary-group weight. When MiraTags is installed, the equipped tag is read from the MiraTags API; otherwise MiraTab can fall back to LuckPerms suffix metadata. Player rows are rendered from the configured format, normally `%prefix%%player%%suffix%`.

The header and footer are configurable and support `%prefix%`, `%suffix%`, `%player%`, `%group%`, `%ping%`, `%world%`, `%online%` and `%max_players%`. Display scope can be `GLOBAL` or `WORLD` for online-count placeholders, while the actual TAB roster remains global so MiraTab does not interfere with player visibility.

Sorting is LuckPerms group-weight aware: higher weighted groups appear first and equal-weight players are sorted alphabetically. The display refreshes on a configurable interval and immediately on join, quit and world changes. MiraTab registers `MiraTabApi` through MiraCore for programmatic refresh/render operations and reports module health through MiraCore.

## Commands

| Command | Permission | What it does |
| --- | --- | --- |
| `/mtab status` | `miratab.admin` | Shows MiraTab version, player count, refresh interval, scope and tag source. |
| `/mtab refresh` | `miratab.admin` | Immediately refreshes the player list for all online players. |
| `/mtab reload` | `miratab.admin` | Reloads configuration and reapplies/restarts the display refresh task. |
| `/mtab test` | `miratab.admin` | Runs MiraTab diagnostics/self-tests. |
| `/mtab help` | `miratab.admin` | Shows MiraTab administration help. |

Full command: `/miratab`. Alias: `/mtab`.

## Permissions

| Permission | Default | What it does |
| --- | --- | --- |
| `miratab.admin` | OP | Allows MiraTab administration, refresh, reload and diagnostics commands. |
