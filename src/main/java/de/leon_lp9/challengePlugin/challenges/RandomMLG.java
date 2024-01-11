package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Random;

@LoadChallenge
public class RandomMLG extends Challenge {

    @ConfigurationValue(title = "RandomMLGFlatWorldName", description = "RandomMLGFlatWorldDescription", icon = Material.GRASS_BLOCK)
    private boolean inFlatWorld = false;
    @ConfigurationValue(title = "RandomMLGGiveWaterBucketName", description = "RandomMLGGiveWaterBucketDescription", icon = Material.WATER_BUCKET)
    private boolean giveWaterBucket = false;
    @ConfigurationValue(title = "RandomMLGTimeBetweenMLGsInMinutesName", description = "RandomMLGTimeBetweenMLGsInMinutesDescription", icon = Material.CLOCK)
    private int timeBetweenMLGsInMinutes = 10;

    public RandomMLG() {
        super(Material.WATER_BUCKET);
    }

    @Override
    public void register() {
        super.register();

        //Wenn die Welt mit dem Namen "MLG_World" nicht existiert, wird sie erstellt
        if (Bukkit.getWorld("MLG_World") == null) {
            Bukkit.createWorld(new org.bukkit.WorldCreator("MLG_World").type(org.bukkit.WorldType.FLAT).generateStructures(false));
            //GameRule "doMobSpawning" wird auf false gesetzt, damit keine Mobs spawnen
            Bukkit.getWorld("MLG_World").setGameRuleValue("doMobSpawning", "false");
        }

    }

    public Player getRandomPlayer() {
        return Bukkit.getOnlinePlayers().stream().findAny().orElse(null);
    }

    //Old Location
    private Map<Player, Location> playerLocations;
    //Old Inventory Contents
    private Map<Player, ItemStack[]> playerInventoryContents;

    @Override
    public void timerTick(int second) {
        super.timerTick(second);

        if (second % 60 * timeBetweenMLGsInMinutes == 60 * timeBetweenMLGsInMinutes - 1) {
            Player player = getRandomPlayer();
            if (player != null) {
                Location getHighestBlockAt = Bukkit.getWorld("MLG_World").getSpawnLocation().getWorld().getHighestBlockAt(Bukkit.getWorld("MLG_World").getSpawnLocation()).getLocation();
                Random r = new Random();
                getHighestBlockAt.setY(getHighestBlockAt.getY() + r.nextInt(200) + 10);

                if (inFlatWorld) {
                    //Save old Location
                    playerLocations.put(player, player.getLocation());
                }

                if (giveWaterBucket) {
                    //Save old Inventory Contents
                    playerInventoryContents.put(player, player.getInventory().getContents());

                    player.teleport(getHighestBlockAt);
                }
            }
        }
    }
}