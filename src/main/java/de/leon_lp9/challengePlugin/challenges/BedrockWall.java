package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

@LoadChallenge
public class BedrockWall extends Challenge {

    transient Set<Integer> blocks = new HashSet<>();

    @ConfigurationValue(title = "BedrockWallSecondsName", icon = Material.CLOCK, min = 1)
    private int sekundenBisBedrock = 10;

    public BedrockWall() {
        super(Material.BEDROCK, ChallengeType.MOVEMENT);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (!isRunning()){
            return;
        }
        Location location = event.getPlayer().getLocation();
        location.setY(0);

        if (!blocks.contains(location.hashCode())){
            blocks.add(location.hashCode());
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {

                //Bedrock von 0 bis Max Y setzen
                for (int y = -64; y < 256; y++) {
                    location.setY(y);
                    location.getBlock().setType(Material.BEDROCK);
                }

                blocks.remove(location.hashCode());
            }, 20L * sekundenBisBedrock);
        }
    }

}
