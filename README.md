# MiraTab

## Version-specific downloads

| Paper version | Java | JAR | Download |
| --- | --- | --- | --- |
| **1.18.2** | 17 | `MiraTab_1.18.2.jar` | [Legacy release](https://github.com/FiveSOCE/Mira-Tab/releases/tag/dev-1.18.2) |
| **1.21.11–26.2** | Java 21 baseline; latest server may require Java 25 | `MiraTab_1.21.11-26.2.jar` | [Modern release](https://github.com/FiveSOCE/Mira-Tab/releases/tag/compat-modern-1.21.11-26.2) |

Use the download links only after CI passes and the named release JAR exists. Runtime testing, as well as Paper 1.19–1.21.10 compatibility, remains outstanding. Do not install both JAR variants.

[Legacy source](https://github.com/FiveSOCE/Mira-Tab/tree/compat/1.18.2) · [Modern source](https://github.com/FiveSOCE/Mira-Tab/tree/main) · [All releases](https://github.com/FiveSOCE/Mira-Tab/releases)

MiraTab formats the player roster for Factions servers using LuckPerms rank prefixes, group-weight sorting, configurable TAB headers/footers and optional MiraTags suffixes.

## Dependencies and integrations

- **Required:** matching Paper and Java runtime, **MiraCore** matching the version and **LuckPerms**.
- **Optional:** MiraTags, matched to your server version, for the active tag suffix; without it, LuckPerms suffix metadata can be used.
- MiraTab has no in-game user commands; its one command and alias are for administrators.

## Commands and permissions

| Command | Permission | Purpose |
| --- | --- | --- |
| `/miratab status` | `miratab.admin` | Show version, display scope, online count and tag source |
| `/miratab refresh` | `miratab.admin` | Refresh all player tab entries |
| `/miratab reload` | `miratab.admin` | Reload configuration and update tasks |
| `/miratab test` | `miratab.admin` | Run diagnostics |
| `/miratab help` | `miratab.admin` | Help |

Alias: `/mtab`. Permission `miratab.admin` defaults to OP.

## Paper 1.18.2 limitation

Paper 1.18.2 does not implement the modern `Player#setPlayerListOrder` API. This build retains tab names, headers, footers, rank and tag formatting, but **native group-weight ordering is not currently implemented** for 1.18.2. The code does not take ownership of global scoreboard teams because that can interfere with MiraFactions and other plugins. A safe legacy scoreboard sort implementation and runtime integration tests are still outstanding.

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
