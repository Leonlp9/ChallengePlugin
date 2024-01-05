package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashSet;
import java.util.Set;

@LoadChallenge
public class TheFloorIsLava extends Challenge {

    transient Set<Integer> blocks = new HashSet<>();

    @ConfigurationValue(title = "Sekunden bis Magma", description = "Sekunden bis der Block zu Magma wird", icon = Material.MAGMA_BLOCK)
    @SuppressWarnings("FieldMayBeFinal")
    private int sekundenBisMagma = 3;
    @ConfigurationValue(title = "Sekunden bis Lava", description = "Sekunden bis der Block zu Lava wird", icon = Material.LAVA_BUCKET)
    @SuppressWarnings("FieldMayBeFinal")
    private int sekundenBisLava = 6;

    public TheFloorIsLava() {
        super("The floor is lava!", "Der Boden ist Lava", Material.LAVA_BUCKET);
        System.out.println("TheFloorIsLava");
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event){
        if (!isRunning()){
            return;
        }
        Location location = event.getPlayer().getLocation().clone().add(0, -1, 0);
        if (location.getBlock().getType() != Material.AIR && location.getBlock().getType() != Material.LAVA && location.getBlock().getType() != Material.MAGMA_BLOCK && !blocks.contains(location.hashCode())){
            blocks.add(location.hashCode());
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                location.getBlock().setType(Material.MAGMA_BLOCK);
                if (sekundenBisLava < sekundenBisMagma){
                    blocks.remove(location.hashCode());
                }
            }, 20L * sekundenBisMagma);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                location.getBlock().setType(Material.LAVA);
                if (sekundenBisLava >= sekundenBisMagma){
                    blocks.remove(location.hashCode());
                }
            }, 20L * sekundenBisLava);
        }
    }

}
