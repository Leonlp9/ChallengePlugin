package de.leon_lp9.challengePlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class PlayerListManager {

    private Map<UUID, String> playerPrefixes;

    public void setPrefix(Player player, String prefix) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            Team team = onlinePlayer.getScoreboard().getTeam(player.getName());
            if (team == null) {
                team = onlinePlayer.getScoreboard().registerNewTeam(player.getName());
            }
            team.setPrefix(prefix);
            team.addEntry(player.getName());
        });
    }

}
