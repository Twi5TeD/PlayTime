
Description:
Playtime is a simple plugin that i made to allow players to view their time played or another player's time played on the server. playtime is fully customizable and can be done from the configuration file.

Player Data:
To reset the player data delete the stats folder inside of the world folder.

Commands/Permissions:
- /playtime - shows your time played on the server - Permission: playtime.check
- /playtimetop - shows the top 10 playtimes on the server - Permission: playtime.check
- /playtime <player> - shows another player's time played on the server - Permission: playtime.check
- /serveruptime - shows the server online time - Permission: playtime.uptime
- /playtimereload - reloads config messages - Permission: playtime.reload
 
  playtime:
aliases: [timeplayed, pt]

  playtimetop:
aliases: [timeplayedtop, ptt]

  serveruptime:
aliases: [uptime, sp]

  playtimereload:
aliases: [ptreload, ptr]
 
 If your a developer and want to add onto the plugin make sure to create a pull request and ill look into it :)