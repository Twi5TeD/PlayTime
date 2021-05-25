
Allow Players to View their playing time.

Description:
Playtime is a simple plugin that i made to allow players to view their time played or another player's time played on the server. playtime is fully customizable and can be done from the configuration file.

Player Data:
To reset the player data delete the stats folder inside of the world folder.

Commands/Permissions:
- /playtime - shows your time played on the server - Permission: playtime.check
- /playtimetop - shows the top 10 playtimes on the server - Permission: playtime.check
- /playtime <player> - shows another player's time played on the server - Permission: playtime.check
- /serveruptime - shows the server online time - Permission: playtime.uptime
 
  playtime:
aliases: [timeplayed, pt]

  playtimetop:
aliases: [timeplayedtop, ptt]

  serveruptime:
aliases: [uptime, sp]

Images:
[ATTACH=full]607137[/ATTACH] [ATTACH=full]607136[/ATTACH] [ATTACH=full]607139[/ATTACH]
[ATTACH=full]607135[/ATTACH]
[ATTACH=full]607138[/ATTACH]

Configuration:
[SPOILER="config.yml"][/SPOILER][SPOILER="config.yml"][/spoiler]
[SPOILER="config.yml"]
# # Playtime By F64_Rx - Need Help? PM me on Spigot or post in the discussion.
# # =================
# # | CONFIGURATION |
# # =================
#
# # available placeholders
# # %playtime_player% - returns the player name
# # %offlineplayer% - returns the offline player name
# # %offlinetime% - shows offline time of a player
# # %offlinetimesjoined% - shows the amount of joins a player has had
# # %playtime_time% - shows time played
# # %playtime_timesjoined% - shows the amount of times the player has joined the server
# # %playtime_serveruptime% - shows the uptime of the server
# # %playtime_top_#_place% - shows the place of the top 10
# # %playtime_top_#_name% - shows the name of the top 10
# # %playtime_top_#_time% - shows the time of the top 10
# # You can also use any other placeholder that PlaceholderAPI supports :)

time:
  second: s
  minute: m
  hour: h
  day: d
messages:
  no_permission:
  - '&8[&bPlayTime&8] &cYou don''t have permission.'
  doesnt_exist:
  - '&8[&bPlayTime&8] &cPlayer %offlineplayer% has not joined before!'
  player:
  - '&b%playtime_player%''s Stats are:'
  - '&bPlayTime: &7%playtime_time%'
  - '&bTimes Joined: &7%playtime_timesjoined%'
  offline_players:
  - '&b%offlineplayer%''s Stats are:'
  - '&bPlayTime: &7%offlinetime%'
  - '&bTimes Joined: &7%offlinetimesjoined%'
  other_players:
  - '&b%playtime_player%''s Stats are:'
  - '&bPlayTime: &7%playtime_time%'
  - '&bTimes Joined: &7%playtime_timesjoined%'
  playtimetop:
    header:
    - '&bTop &e10 &bplayers playtime:'
    - ''
    message:
    - '&a%position%. &b%player%: &e%playtime%'
    footer:
    - ''
  server_uptime:
  - '&8[&bPlayTime&8] &bServer''s total uptime is %playtime_serveruptime%'

[/SPOILER]

PlaceholderAPI Placeholders:
%playtime_player% - the player name
%playtime_time% - shows time played
%playtime_timesjoined% - shows times joined
%playtime_serveruptime% - shows the uptime of the server
%playtime_top_#_place% - shows the place of the top 10
%playtime_top_#_name% - shows the name of the top 10
%playtime_top_#_time% - shows the time of the top 10

Links:
Needed:
PlaceholderAPI
If you want to use placeholders in holograhpic display you need
HolographicDisplay
ProtocolLib
HolographicExtension

If you find any problems please let me know in the discussion page and not in the review section thanks.
