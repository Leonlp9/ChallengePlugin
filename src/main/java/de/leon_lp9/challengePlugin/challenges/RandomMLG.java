package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@LoadChallenge
public class RandomMLG extends Challenge {

    public enum RandomMLGItemType {
        WaterBucket(Material.WATER_BUCKET),
        //SlimeBlock(Material.SLIME_BLOCK),
        Boat(Material.OAK_BOAT),
        EnderPearl(Material.ENDER_PEARL),
        RandomItem(Material.DIAMOND);

        private Material material;
        RandomMLGItemType(Material material) {
            this.material = material;
        }

        public Material getMaterial() {
            if (this == RandomItem) {
                Random r = new Random();
                return RandomMLGItemType.values()[r.nextInt(RandomMLGItemType.values().length - 1)].getMaterial();
            }else {
                return material;
            }
        }

    }

    @ConfigurationValue(title = "RandomMLGFlatWorldName", description = "RandomMLGFlatWorldDescription", icon = Material.GRASS_BLOCK)
    private boolean inFlatWorld = true;
    @ConfigurationValue(title = "RandomMLGGiveWaterBucketName", description = "RandomMLGGiveWaterBucketDescription", icon = Material.WATER_BUCKET)
    private boolean giveWaterBucket = true;
    @ConfigurationValue(title = "RandomMLGTimeBetweenMLGsInMinutesName", description = "RandomMLGTimeBetweenMLGsInMinutesDescription", icon = Material.CLOCK)
    private int timeBetweenMLGsInMinutes = 10;
    @ConfigurationValue(title = "RandomMLGItemType", description = "RandomMLGItemTypeDescription", icon = Material.DIAMOND)
    private RandomMLGItemType randomMLGItemType = RandomMLGItemType.WaterBucket;


    //Old Location
    private transient Map<Player, Location> playerLocations;
    //Old Inventory Contents
    private transient Map<Player, ItemStack[]> playerInventoryContents;
    private transient boolean isDead = false;

    public RandomMLG() {
        super(Material.WATER_BUCKET);
        playerLocations = new HashMap<>();
        playerInventoryContents = new HashMap<>();
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

    @Override
    public void unregister() {
        super.unregister();

        //Wenn die Welt mit dem Namen "MLG_World" existiert, wird sie gelöscht
        if (Bukkit.getWorld("MLG_World") != null) {
            Bukkit.unloadWorld("MLG_World", false);
        }

    }


    public Player getRandomPlayer() {
        return Bukkit.getOnlinePlayers().stream().findAny().orElse(null);
    }

    @Override
    public void timerTick(int second) {
        super.timerTick(second);

        //new sync Thread because of the method is called async
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            if (second % 60 * timeBetweenMLGsInMinutes == 60 * timeBetweenMLGsInMinutes - 1) {
                Player player = getRandomPlayer();
                if (player != null) {

                    Main.getInstance().getChallengeManager().getTimer().setResumed(false);

                    Location getHighestBlockAt;

                    if (inFlatWorld) {
                        //Save old Location
                        getHighestBlockAt = Bukkit.getWorld("MLG_World").getSpawnLocation().getWorld().getHighestBlockAt(Bukkit.getWorld("MLG_World").getSpawnLocation()).getLocation();
                        playerLocations.put(player, player.getLocation());
                    }else {
                        getHighestBlockAt = player.getWorld().getSpawnLocation().getWorld().getHighestBlockAt(player.getWorld().getSpawnLocation()).getLocation();
                    }
                    getHighestBlockAt.setX(getHighestBlockAt.getX() + 0.5);
                    getHighestBlockAt.setZ(getHighestBlockAt.getZ() + 0.5);

                    Random r = new Random();
                    getHighestBlockAt.setY(getHighestBlockAt.getY() + r.nextInt(200) + 10);

                    if (giveWaterBucket) {
                        //Save old Inventory Contents
                        playerInventoryContents.put(player, player.getInventory().getContents());
                        player.getInventory().clear();
                        Material material = randomMLGItemType.getMaterial();
                        for (int i = 0; i < 9; i++) {
                            player.getInventory().setItem(i, new ItemStack(material));
                        }

                        player.teleport(getHighestBlockAt);
                        player.setVelocity(player.getVelocity().setY(0.5));
                    }

                    Bukkit.getOnlinePlayers().forEach(player1 -> {
                        if (player1 != player) {
                            Location highestBlockAtPlayer = player1.getWorld().getHighestBlockAt(player1.getLocation()).getLocation();
                            highestBlockAtPlayer.setY(highestBlockAtPlayer.getY() + 2);
                            highestBlockAtPlayer.setX(highestBlockAtPlayer.getX() + 0.5);
                            highestBlockAtPlayer.setZ(highestBlockAtPlayer.getZ() + 0.5);
                            player1.teleport(highestBlockAtPlayer);
                            player1.setGameMode(org.bukkit.GameMode.SPECTATOR);
                        }
                    });
                }
            }
        });
    }

    //Wassereimer
    @EventHandler
    public void onPlayerInteract(PlayerBucketEmptyEvent event) {
        if (event.getPlayer().getWorld().getName().equals("MLG_World")) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                event.getBlock().setType(Material.AIR);
            }, 20);
        }
    }

    //Slimeblock
    @EventHandler
    public void onPlayerInteract(BlockPlaceEvent event) {
        if (event.getPlayer().getWorld().getName().equals("MLG_World")) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                event.getBlock().setType(Material.AIR);
            }, 20);
        }
    }

    //Boat
    @EventHandler
    public void onPlayerInteract(EntityPlaceEvent event) {
        if (event.getEntity().getWorld().getName().equals("MLG_World")) {
            if (event.getEntity().getType().equals(EntityType.BOAT)) {
                Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                    event.getEntity().remove();
                }, 20);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(BlockBreakEvent event) {
        if (event.getPlayer().getWorld().getName().equals("MLG_World")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e){
        isDead = true;
    }

}