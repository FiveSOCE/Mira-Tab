# MiraTab

## Downloads and compatibility

| Server target | Java | Final JAR name | Download |
| --- | --- | --- | --- |
| **Paper 1.18.2** | 17 | `MiraTab_1.18.2.jar` | [Download 1.18.2 JAR](https://github.com/FiveSOCE/Mira-Tab/releases/download/dev-1.18.2/MiraTab_1.18.2.jar) |
| **Paper 1.21.11–26.2** | Java 21 baseline; Paper 26.2 may require Java 25 | `MiraTab_1.21.11-26.2.jar` | [Download modern JAR](https://github.com/FiveSOCE/Mira-Tab/releases/download/compat-modern-1.21.11-26.2/MiraTab_1.21.11-26.2.jar) |

**Build status:** Version-specific direct links are provided where published. A GitHub Actions or Releases link means the corresponding versioned JAR has not been verified. Compilation alone does not establish in-game compatibility; intermediate versions 1.19–1.21.10 remain unverified unless specifically documented.

**Installation:** Back up plugin data, remove older JAR copies and install only the build matching your server. Never install both builds together.

[Legacy source](https://github.com/FiveSOCE/Mira-Tab/tree/compat/1.18.2) · [Modern source](https://github.com/FiveSOCE/Mira-Tab) · [All releases](https://github.com/FiveSOCE/Mira-Tab/releases)

## Dependencies and installation

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
