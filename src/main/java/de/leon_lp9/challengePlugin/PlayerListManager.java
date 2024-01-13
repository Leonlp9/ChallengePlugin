package de.leon_lp9.challengePlugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerListManager implements Listener {

    public PlayerListManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private Map<UUID, String> playerPrefixes = new HashMap<>();

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
        Player p = event.getPlayer();

        try {
            Objective objectivetab = null;
            objectivetab = p.getScoreboard().registerNewObjective("❤", "health");
            objectivetab.setDisplaySlot(DisplaySlot.PLAYER_LIST);
            objectivetab.setDisplayName("§c❤");
            objectivetab.setRenderType(RenderType.HEARTS);
        }catch (Exception ignored) {

        }

        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            if (playerPrefixes.containsKey(onlinePlayer.getUniqueId())) {
                setPrefix(onlinePlayer, playerPrefixes.get(onlinePlayer.getUniqueId()));
            }
        });
    }

}
