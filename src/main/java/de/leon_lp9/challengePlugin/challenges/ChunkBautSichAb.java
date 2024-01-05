package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import org.bukkit.Bukkit;
import org.bukkit.Material;

@LoadChallenge
public class ChunkBautSichAb extends Challenge {
    private transient int taskID = 0;

    @ConfigurationValue(title = "Sekunden bis Abbau", description = "Sekunden bis der Chunk abgebaut wird", icon = Material.CLOCK)
    private int sekundenBisAbbau = 10;

    public ChunkBautSichAb() {
        super("Chunk Baut Sich Ab", "Der Chunk baut sich ab", Material.DIAMOND_PICKAXE);
    }

    @Override
    public void update() {
        super.update();
        startTaskTimer();
    }

    @Override
    public void unregister() {
        super.unregister();
        Bukkit.getScheduler().cancelTask(taskID);
    }

    @Override
    public void register() {
        super.register();
        startTaskTimer();
    }

    public void startTaskTimer() {
        if (taskID != 0) Bukkit.getScheduler().cancelTask(taskID);
        taskID = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            if (!isRunning()) {
                return;
            }
            Bukkit.getOnlinePlayers().forEach(player -> {
                //bei jedem Spieler den obersten block des chunks entfernen

                for (int x = player.getLocation().getChunk().getX() * 16; x < player.getLocation().getChunk().getX() * 16 + 16; x++) {
                    for (int z = player.getLocation().getChunk().getZ() * 16; z < player.getLocation().getChunk().getZ() * 16 + 16; z++) {
                        if (player.getWorld().getBlockAt(x, player.getWorld().getHighestBlockYAt(x, z), z).getType() == Material.BEDROCK) {
                            continue;
                        }
                        player.getWorld().getBlockAt(x, player.getWorld().getHighestBlockYAt(x, z), z).setType(Material.AIR);
                    }
                }
            });
        }, 0L, 20L * sekundenBisAbbau).getTaskId();
    }
}
