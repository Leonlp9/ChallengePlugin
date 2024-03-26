package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.FallingBlock;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

@LoadChallenge
public class BlockeFliegenInDieLuft extends Challenge {

    @ConfigurationValue(icon = Material.CLOCK, title = "ZeitBisBlockFliegt", min = 1, max = 100)
    private int zeitBisBlockFliegt = 20;

    public BlockeFliegenInDieLuft() {
        super(Material.SAND, ChallengeType.MOVEMENT);
    }

    @EventHandler()
    public void onMove(PlayerMoveEvent event) {
        if (isRunning()) {
            if (event.getTo() != null) {
                Block blockBelow = event.getTo().getBlock().getRelative(0, -1, 0);
                if (blockBelow != null) {
                    if (blockBelow.getType().isSolid()) {
                        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                            this.boostBlockInAir(blockBelow);
                        }, this.zeitBisBlockFliegt);
                    }
                }
            }
        }
    }

    private void boostBlockInAir(Block block) {
        if (block.getType().isSolid()) {
            FallingBlock fallingBlock = block.getWorld().spawnFallingBlock(block.getLocation().add(0.5, 0.0, 0.5), block.getBlockData());
            fallingBlock.setInvulnerable(true);
            fallingBlock.setVelocity(new Vector(0, 1, 0));
            block.setType(Material.AIR);
        }
    }

}
