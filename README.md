# minecraft-api

Shared interfaces for the SportBukkit and BungeeCord (OCN fork) APIs

These interfaces can be used to write hybrid code that works with both APIs.
Currently, the following things are implemented, roughly speaking:

  * Plugin interface with lifecycle methods
  * Plugin metadata and lookup
  * Plugin configuration
  * Loggers
  * Online player list/search
  * Getting player name and locale
  * Sending component messages to players/console
  * Very basic permission checking
  * Event listener registration

Possible future additions:

  * Full event system
  * Task scheduler
  * Plugin channels
  * Server configuration (name, port, etc)
  * Commands
  * Scoreboard
  * Tab list

https://github.com/OvercastNetwork/BungeeCord

https://github.com/OvercastNetwork/SportBukkit
