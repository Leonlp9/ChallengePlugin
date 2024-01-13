package de.leon_lp9.challengePlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.Team;

import java.awt.*;
import java.util.Map;
import java.util.UUID;

public class PlayerListManager implements Listener {

    public PlayerListManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());


    }

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

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            if (playerPrefixes.containsKey(onlinePlayer.getUniqueId())) {
                setPrefix(onlinePlayer, playerPrefixes.get(onlinePlayer.getUniqueId()));
            }
        });
    }

}
