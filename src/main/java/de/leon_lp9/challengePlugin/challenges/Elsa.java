package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import java.util.ArrayList;
import java.util.List;

@LoadChallenge
public class Elsa extends Challenge {

    private transient List<Player> playerWhoToggeledOffIce = new ArrayList<>();

    @ConfigurationValue(title = "ElsaIceRadiusName", icon = Material.BAMBOO_BUTTON, min = 1, max = 10)
    private int iceRadius = 3;

    public Elsa() {
        super(Material.PACKED_ICE);
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event){
        if (!isRunning()){
            return;
        }
        if (event.isSneaking() && playerWhoToggeledOffIce.contains(event.getPlayer())){
            playerWhoToggeledOffIce.remove(event.getPlayer());
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1,1.2f);
            //stop player falling
            event.getPlayer().setVelocity(event.getPlayer().getVelocity().setY(0.001));
            event.getPlayer().setFallDistance(0);
        } else if (event.isSneaking()){
            playerWhoToggeledOffIce.add(event.getPlayer());
            event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.BLOCK_NOTE_BLOCK_BANJO, 1, 0.8f);
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!isRunning()) {
            return;
        }
        if (playerWhoToggeledOffIce.contains(event.getPlayer())){
            return;
        }
        Location location = event.getPlayer().getLocation().clone().add(0, -1, 0);

        for (int x = location.getBlockX() - iceRadius; x <= location.getBlockX() + iceRadius; x++) {
            for (int z = location.getBlockZ() - iceRadius; z <= location.getBlockZ() + iceRadius; z++) {
                // Berechnen Sie die Entfernung des aktuellen Blocks vom Mittelpunkt des Kreises.
                int distanceSquared = (x - location.getBlockX()) * (x - location.getBlockX()) +
                        (z - location.getBlockZ()) * (z - location.getBlockZ());

                // Überprüfen Sie, ob der Block innerhalb des Kreises liegt.
                if (distanceSquared <= (iceRadius * iceRadius)) {
                    location.getWorld().getBlockAt(x, location.getBlockY(), z).setType(Material.PACKED_ICE);
                }
            }
        }

    }

}
