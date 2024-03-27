package de.leon_lp9.challengePlugin.challenges;

import com.google.common.collect.Lists;
import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import de.leon_lp9.challengePlugin.management.Triple;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@LoadChallenge
public class InAllenChunksPassiertDasGleiche extends Challenge {

    private transient final Map<String, Map<Triple<Integer, Integer, Integer>, BlockData>> changedBlocks = new HashMap();
    private transient final Set<Chunk> updatedChunks = new HashSet();

    public InAllenChunksPassiertDasGleiche() {
        super(Material.GRASS_BLOCK, ChallengeType.BLOCKS);
    }

    private void changeBlockInEveryChunk(World world, int x, int y, int z, BlockData data, Block exception) {
        this.updatedChunks.clear();
        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Map<Triple<Integer, Integer, Integer>, BlockData> map = (Map)this.changedBlocks.getOrDefault(world.getName(), new HashMap());
            this.changedBlocks.putIfAbsent(world.getName(), map);
            map.put(new Triple(x, y, z), data);
            Iterator var8 = world.getPlayers().iterator();

            while(var8.hasNext()) {
                Player player = (Player)var8.next();
                Iterator var10 = this.getSurroundingChunks(player.getLocation().getChunk(), false).iterator();

                while(var10.hasNext()) {
                    Chunk chunk = (Chunk)var10.next();
                    Block block = chunk.getBlock(x, y, z);
                    if (!Objects.equals(exception, block)) {
                        this.setBlockData(block, data, true);
                    }
                }
            }

        });
    }

    private List<Chunk> getSurroundingChunks(Chunk center, boolean onlyBorder) {
        int range = 8;
        int centerX = center.getX();
        int centerZ = center.getZ();
        List<Chunk> chunks = Lists.newLinkedList();

        for(int x = -range; x <= range; ++x) {
            for(int z = -range; z <= range; ++z) {
                if (!onlyBorder || x == -range || x == range || z == -range || z == range) {
                    Chunk chunkAt = center.getWorld().getChunkAt(centerX + x, centerZ + z);
                    if (!this.updatedChunks.contains(chunkAt)) {
                        chunks.add(chunkAt);
                    }
                }
            }
        }

        return chunks;
    }

    private void updateSurroundingChunks(Chunk center, boolean onlyBorder) {
        Iterator var3 = this.getSurroundingChunks(center, onlyBorder).iterator();

        while(var3.hasNext()) {
            Chunk chunk = (Chunk)var3.next();
            this.updateChunk(chunk);
        }

    }

    private void updateChunk(Chunk chunk) {
        this.updatedChunks.add(chunk);
        Map<Triple<Integer, Integer, Integer>, BlockData> dataMap = (Map)this.changedBlocks.getOrDefault(chunk.getWorld().getName(), new HashMap());
        Iterator var3 = dataMap.entrySet().iterator();

        while(var3.hasNext()) {
            Map.Entry<Triple<Integer, Integer, Integer>, BlockData> blockEntry = (Map.Entry)var3.next();
            Triple<Integer, Integer, Integer> pos = (Triple)blockEntry.getKey();
            BlockData blockData = (BlockData)blockEntry.getValue();
            Block block = chunk.getBlock((Integer)pos.getFirst(), (Integer)pos.getSecond(), (Integer)pos.getThird());
            if (!block.getBlockData().matches(blockData)) {
                this.setBlockData(block, blockData, false);
            }
        }

    }

    private void setBlockData(Block block, BlockData blockData, boolean update) {
        if (block.getType() != Material.END_PORTAL && block.getType() != Material.END_PORTAL_FRAME && block.getType() != Material.END_GATEWAY) {
            block.setBlockData(blockData, update);
        }
    }

    private int getRelativeChunkCoordinate(int worldCoordinate) {
        return worldCoordinate & 15;
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();
        this.changeBlockInEveryChunk(event.getBlock().getWorld(), this.getRelativeChunkCoordinate(block.getX()), block.getY(), this.getRelativeChunkCoordinate(block.getZ()), block.getBlockData(), event.getBlock());
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        this.changeBlockInEveryChunk(event.getBlock().getWorld(), this.getRelativeChunkCoordinate(block.getX()), block.getY(), this.getRelativeChunkCoordinate(block.getZ()), Bukkit.createBlockData(Material.AIR), event.getBlock());
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onMove(PlayerMoveEvent event) {
        if (event.getTo() != null) {
            if (event.getTo().getChunk() != event.getFrom().getChunk()) {
                this.updateSurroundingChunks(event.getTo().getChunk(), true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onMove(VehicleMoveEvent event) {
        boolean anyMatch = false;
        Iterator var3 = event.getVehicle().getPassengers().iterator();

        while(var3.hasNext()) {
            Entity passenger = (Entity)var3.next();
            if (passenger instanceof Player) {
                anyMatch = true;
                break;
            }
        }

        if (anyMatch) {
            if (event.getTo().getChunk() != event.getFrom().getChunk()) {
                this.updateSurroundingChunks(event.getTo().getChunk(), true);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void onTeleport(PlayerTeleportEvent event) {
        if (event.getTo() != null) {
            if (event.getTo().getChunk() != event.getFrom().getChunk()) {
                this.updateSurroundingChunks(event.getTo().getChunk(), false);
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onChunkUnload(ChunkUnloadEvent event) {
        this.updatedChunks.remove(event.getChunk());
    }
    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onExplode(EntityExplodeEvent event) {
        event.blockList().forEach(block -> {
            this.changeBlockInEveryChunk(block.getWorld(), this.getRelativeChunkCoordinate(block.getX()), block.getY(), this.getRelativeChunkCoordinate(block.getZ()), Bukkit.createBlockData(Material.AIR), block);
        });
    }

}
