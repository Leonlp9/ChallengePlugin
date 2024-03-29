package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@LoadChallenge
public class NoPressurePlate extends Challenge{
    
    private transient final Random random = new Random();
    
    @Getter
    @ConfigurationValue(title = "Range", icon = Material.TRIDENT, min = 2, max = 10)
    private int range = 4;

    @Getter
    @ConfigurationValue(title = "Count", icon = Material.HEAVY_WEIGHTED_PRESSURE_PLATE, min = 10, max = 256)
    private int count = 100;

    private transient ArrayList<Chunk> chunks = new ArrayList<>();

    public NoPressurePlate() {
        super(Material.HEAVY_WEIGHTED_PRESSURE_PLATE, ChallengeType.BLOCKS);
    }

    @Override
    public void unregister() {
        super.unregister();
        this.removePressurePlates();
    }

    @Override
    public void unload() {
        super.unload();
        this.removePressurePlates();
    }

    private void removePressurePlates() {
        Iterator var1 = Bukkit.getWorlds().iterator();

        while(var1.hasNext()) {
            World world = (World)var1.next();
            Iterator var3 = world.getEntitiesByClass(FallingBlock.class).iterator();

            while(var3.hasNext()) {
                FallingBlock entity = (FallingBlock)var3.next();
                String name = entity.getBlockData().getMaterial().name();
                if (name.contains("PressurePlate")) {
                    entity.remove();
                }
            }
        }

    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        if (isRunning()) {
            Player player = e.getPlayer();
            List<Chunk> targetChunks = this.getTargetChunks(player.getLocation().getChunk());
            Iterator var5 = targetChunks.iterator();

            while (var5.hasNext()) {
                Chunk targetChunk = (Chunk) var5.next();
                if (!chunks.contains(targetChunk)) {
                    chunks.add(targetChunk);
                    this.spawnPressurePlates(targetChunk, 5);
                }
            }
        }
    }

    private void spawnPressurePlates(@Nonnull Chunk chunk, int height) {
        for(int i = 0; i < this.getCount(); ++i) {
            Block block = this.getRandomBlockInChunk(chunk, height);
            Location location = block.getLocation().add(0.5, 0.0, 0.5);
            location.setY(location.getWorld().getHighestBlockYAt(location) + height);
            block.getWorld().spawnFallingBlock(location, Material.HEAVY_WEIGHTED_PRESSURE_PLATE, (byte)0);
        }

    }

    private List<Chunk> getTargetChunks(@Nonnull Chunk origin) {
        List<Chunk> chunks = new ArrayList<>();
        int originX = origin.getX();
        int originZ = origin.getZ();
        int range = this.getRange();

        for(int x = -range; x <= range; ++x) {
            for(int z = -range; z <= range; ++z) {
                Chunk chunkAt = origin.getWorld().getChunkAt(originX + x, originZ + z);
                chunks.add(chunkAt);
            }
        }

        return chunks;
    }

    private Block getRandomBlockInChunk(@Nonnull Chunk chunk, int y) {
        int x = this.random.nextInt(16);
        int z = this.random.nextInt(16);
        return chunk.getBlock(x, y, z);
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onEntityChangeBlock(@Nonnull EntityChangeBlockEvent event) {
        if (event.getEntity() instanceof FallingBlock) {
            String name = ((FallingBlock)event.getEntity()).getBlockData().getMaterial().name();
            if (name.contains("PressurePlate")) {
                Block block = event.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (!block.getType().isAir()) {
                    event.getBlock().setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onDrop(@Nonnull EntityDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
            if (event.getEntityType() == EntityType.FALLING_BLOCK) {
                String name = ((FallingBlock)event.getEntity()).getBlockData().getMaterial().name();
                if (name.contains("PRESSURE_PLATE")) {
                    event.getEntity().getLocation().getBlock().setType(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
                    event.setCancelled(true);
                }
            }
        }
    }
    
    @EventHandler
    public void onPressurePlate(PlayerInteractEvent event) {
        if (isRunning()) {
            if (event.getAction().equals(Action.PHYSICAL)) {
                if (event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                    event.setCancelled(true);
                    Vector vector = event.getPlayer().getLocation().getDirection().multiply(-2);
                    vector.setY(1);
                    event.getPlayer().setVelocity(vector);
                    event.getPlayer().setHealth(0.0);
                    event.getClickedBlock().getWorld().createExplosion(event.getClickedBlock().getLocation().clone().add(0.5, 0.5, 0.5), 4, false, true);
                    event.getClickedBlock().setType(Material.AIR);
                }
            }
        }
    }

    @EventHandler
    public void onEntityInteract(EntityInteractEvent event) {
        if (isRunning()) {
            if (event.getEntity().getType() == EntityType.FALLING_BLOCK || event.getEntity().getType() == EntityType.PLAYER || event.getEntity().getType() == EntityType.ENDER_DRAGON) return;
            if (event.getBlock() != null && event.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().createExplosion(event.getBlock().getLocation().clone().add(0.5,0.5,0.5), 2, false, false);
                event.getEntity().remove();
            }
        }
    }

    @EventHandler
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (isRunning()) {
            if (event.getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().createExplosion(event.getBlock().getLocation().clone().add(0.5,0.5,0.5), 4, false, false);
            }
            else if (event.getBlock().getLocation().add(0,1,0).getBlock().getType() == Material.HEAVY_WEIGHTED_PRESSURE_PLATE) {
                event.setCancelled(true);
                event.getBlock().setType(Material.AIR);
                event.getBlock().getWorld().createExplosion(event.getBlock().getLocation().clone().add(0.5,0.5,0.5), 2, false, false);
            }
        }
    }

}
