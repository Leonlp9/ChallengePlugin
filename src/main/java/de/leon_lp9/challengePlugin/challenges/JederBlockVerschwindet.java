package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.checkerframework.checker.units.qual.C;

@LoadChallenge
public class JederBlockVerschwindet extends Challenge {

    @ConfigurationValue(title = "Block Platzieren", description = "Blöcke verschwinden beim Platzieren", icon = Material.GRASS_BLOCK)
    private boolean blockPlace = true;
    @ConfigurationValue(title = "Block Abbauen", description = "Blöcke verschwinden beim Abbauen", icon = Material.DIAMOND_PICKAXE)
    private boolean blockBreak = true;

    public JederBlockVerschwindet() {
        super("Jeder Block verschwindet", "Jeder Block mit dem du interagierst verschwindet aus dem Chunk", Material.STRUCTURE_VOID);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (!isRunning()){
            return;
        }
        if (blockPlace){
            //Block an den drann geklickt wurde von dem den Type alle aus dem Chunk entfernen
            Block block = event.getBlockAgainst();

            removeBlockFromChunk(block, false);

        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (!isRunning()){
            return;
        }
        if (blockBreak){
            //Block der abgebaut wurde von dem den Type alle aus dem Chunk entfernen
            Block block = event.getBlock();

            removeBlockFromChunk(block, true);
        }
    }

    private void removeBlockFromChunk(Block block, boolean dropItem) {
        Chunk chunk = block.getChunk();
        for (int x = chunk.getX() * 16; x < chunk.getX() * 16 + 16; x++) {
            for (int z = chunk.getZ() * 16; z < chunk.getZ() * 16 + 16; z++) {
                for (int y = -64; y < 256; y++) {
                    if (block.getWorld().getBlockAt(x, y, z).getType() == block.getType()){
                        if (dropItem) block.getWorld().dropItemNaturally(block.getLocation(), block.getDrops().iterator().next());
                        block.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }
            }
        }
    }

}
