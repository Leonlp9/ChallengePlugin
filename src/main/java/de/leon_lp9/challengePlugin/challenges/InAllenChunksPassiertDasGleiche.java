package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;

public class InAllenChunksPassiertDasGleiche extends Challenge {

    //eine liste mit cords im chunk wo ein block platziert wurde und welcher block type es ist
    private final Map<Integer, Material> placedBlocks = new HashMap<>();

    public InAllenChunksPassiertDasGleiche() {
        super(Material.STRUCTURE_BLOCK);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event){
        if (!isRunning()){
            return;
        }
        //bei welchen coords im chunk der block platziert wurde
        int x = event.getBlock().getX() % 16;
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ() % 16;

//        Location chunkLocation = new Location(event.getBlock().getWorld(), x, y, z);
//        placedBlocks.put(chunkLocation.hashCode(), event.getBlock().getType());

        for (Chunk loadedChunk : event.getPlayer().getWorld().getLoadedChunks()) {
            if (loadedChunk.getX() == event.getBlock().getChunk().getX() && loadedChunk.getZ() == event.getBlock().getChunk().getZ()){
                continue;
            }
            if (loadedChunk.getBlock(x, y, z).getType() == event.getBlock().getType()){
                continue;
            }
            loadedChunk.getBlock(x, y, z).setType(event.getBlock().getType());
            loadedChunk.getBlock(x, y, z).setBlockData(event.getBlock().getBlockData());
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event){
        if (!isRunning()){
            return;
        }
        //bei welchen coords im chunk der block platziert wurde
        int x = event.getBlock().getX() % 16;
        int y = event.getBlock().getY();
        int z = event.getBlock().getZ() % 16;

//        Location chunkLocation = new Location(event.getBlock().getWorld(), x, y, z);
//        placedBlocks.put(chunkLocation.hashCode(), Material.AIR);

        for (Chunk loadedChunk : event.getPlayer().getWorld().getLoadedChunks()) {
            if (loadedChunk.getX() == event.getBlock().getChunk().getX() && loadedChunk.getZ() == event.getBlock().getChunk().getZ()){
                continue;
            }
            if (loadedChunk.getBlock(x, y, z).getType() == Material.AIR){
                continue;
            }
            loadedChunk.getBlock(x, y, z).setType(Material.AIR);
        }

    }

}
