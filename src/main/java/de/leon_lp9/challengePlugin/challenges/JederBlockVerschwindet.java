package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
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

    @ConfigurationValue(title = "Block Drop beim Abbauen", description = "Blöcke droppen beim Abbauen", icon = Material.EGG)
    private boolean blockDropByBreak = true;
    @ConfigurationValue(title = "Block Drop beim Platzieren", description = "Blöcke droppen beim Platzieren", icon = Material.EGG)
    private boolean blockDropByPlace = false;

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

            removeBlockFromChunk(block, blockDropByPlace);

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

            removeBlockFromChunk(block, blockDropByBreak);
        }
    }

    private void removeBlockFromChunk(Block block, boolean dropItem) {
        Material material = block.getType();
        Location location = block.getLocation();
        for (int x = location.getChunk().getX() * 16; x < location.getChunk().getX() * 16 + 16; x++) {
            for (int z = location.getChunk().getZ() * 16; z < location.getChunk().getZ() * 16 + 16; z++) {
                for (int y = -64; y < 256; y++) {
                    if (block.getWorld().getBlockAt(x, y, z).getType() == material){
                        if (dropItem) {

                            //wenn der block minderstens 1 item droppt
                            if (!location.getWorld().getBlockAt(x, y, z).getDrops().isEmpty()) {
                                //alle items droppen
                                location.getWorld().getBlockAt(x, y, z).getDrops().forEach(itemStack -> location.getWorld().dropItemNaturally(location, itemStack));
                            }
                        }
                        if (material == Material.BEDROCK){
                            continue;
                        }
                        block.getWorld().getBlockAt(x, y, z).setType(Material.AIR);
                    }
                }
            }
        }
    }

}
