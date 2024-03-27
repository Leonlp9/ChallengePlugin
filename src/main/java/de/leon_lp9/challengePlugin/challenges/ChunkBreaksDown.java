package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;

@LoadChallenge
public class ChunkBreaksDown extends Challenge {
    private transient int taskID = 0;

    @ConfigurationValue(title = "SecondsUntilChunkBreaksDownName", icon = Material.CLOCK, min = 1)
    private int sekundenBisAbbau = 10;

    public ChunkBreaksDown() {
        super(Material.DIAMOND_PICKAXE, ChallengeType.BLOCKS);
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
        taskID = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
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
