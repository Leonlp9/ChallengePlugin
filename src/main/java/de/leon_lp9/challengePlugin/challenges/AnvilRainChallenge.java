package de.leon_lp9.challengePlugin.challenges;

import de.leon_lp9.challengePlugin.Main;
import de.leon_lp9.challengePlugin.challenges.config.ConfigurationValue;
import de.leon_lp9.challengePlugin.challenges.config.LoadChallenge;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDropItemEvent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@LoadChallenge
public class AnvilRainChallenge extends Challenge{

    private transient final Random random = new Random();

    @Getter
    @ConfigurationValue(title = "Damage", icon = Material.DIAMOND_SWORD, min = 1, max = 20)
    private int damage = 2;

    @Getter
    @ConfigurationValue(title = "Range", icon = Material.TRIDENT, min = 1, max = 10)
    private int range = 5;

    @Getter
    @ConfigurationValue(title = "Count", icon = Material.BAMBOO, min = 1, max = 20)
    private int count = 5;

    public AnvilRainChallenge() {
        super(Material.ANVIL, ChallengeType.BLOCKS);
    }

    @Override
    public void unregister() {
        super.unregister();
        this.removeAnvils();
    }

    @Override
    public void unload() {
        super.unload();
        this.removeAnvils();
    }

    private void removeAnvils() {
        Iterator var1 = Bukkit.getWorlds().iterator();

        while(var1.hasNext()) {
            World world = (World)var1.next();
            Iterator var3 = world.getEntitiesByClass(FallingBlock.class).iterator();

            while(var3.hasNext()) {
                FallingBlock entity = (FallingBlock)var3.next();
                String name = entity.getBlockData().getMaterial().name();
                if (name.contains("ANVIL")) {
                    entity.remove();
                }
            }
        }

    }
    @Override
    public void timerTick(int seconds) {
        super.timerTick(seconds);

        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
        this.handleTimeActivation();
        });
    }

    private void handleTimeActivation() {
        List<Chunk> chunks = new ArrayList();
        Iterator var2 = Bukkit.getOnlinePlayers().iterator();

        while(true) {
            Player player;
            if (!var2.hasNext()) {
                return;
            }

            player = (Player)var2.next();

            List<Chunk> targetChunks = this.getTargetChunks(player.getLocation().getChunk());
            Iterator var5 = targetChunks.iterator();

            while(var5.hasNext()) {
                Chunk targetChunk = (Chunk)var5.next();
                if (!chunks.contains(targetChunk)) {
                    chunks.add(targetChunk);
                    this.spawnAnvils(targetChunk, player.getLocation().getBlockY() + 50);
                }
            }
        }
    }

    private void spawnAnvils(@Nonnull Chunk chunk, int height) {
        for(int i = 0; i < this.getCount(); ++i) {
            Block block = this.getRandomBlockInChunk(chunk, height);
            Location location = block.getLocation().add(0.5, 0.0, 0.5);
            block.getWorld().spawnFallingBlock(location, Material.ANVIL, (byte)0);
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
            if (name.contains("ANVIL")) {
                Block block = event.getBlock().getLocation().subtract(0.0, 1.0, 0.0).getBlock();
                if (!block.getType().isAir()) {
                    event.getBlock().setType(Material.AIR);
                    event.setCancelled(true);
                    event.getEntity().remove();
                    this.destroyRandomBlocks(event.getBlock().getLocation());
                    this.applyDamageToNearEntities(event.getBlock().getLocation().add(0.5, 0.5, 0.5));
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.HIGH,
            ignoreCancelled = true
    )
    public void onDrop(@Nonnull EntityDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getType() == Material.ANVIL) {
            if (event.getEntityType() == EntityType.FALLING_BLOCK) {
                String name = ((FallingBlock)event.getEntity()).getBlockData().getMaterial().name();
                if (name.contains("ANVIL")) {
                    event.setCancelled(true);
                    this.destroyRandomBlocks(event.getEntity().getLocation());
                    this.applyDamageToNearEntities(event.getEntity().getLocation().getBlock().getLocation().add(0.5, 0.5, 0.5));
                }
            }
        }
    }

    public void destroyRandomBlocks(@Nonnull Location origin) {
        int i = this.random.nextInt(2);
        if (i != 0) {
            int blocks = this.getCount() < 16 ? 0 : 1;

            while(true) {
                if (blocks < 2 && origin.getBlockY() > 1) {
                    if (origin.getBlock().getType() != Material.WATER && origin.getBlock().getType() != Material.LAVA) {
                        origin.subtract(0.0, 1.0, 0.0);
                        if (origin.getBlock().isPassable()) {
                            --blocks;
                        }

                        origin.getBlock().setType(Material.AIR);
                        ++blocks;
                        continue;
                    }

                    return;
                }

                return;
            }
        }
    }

    public void applyDamageToNearEntities(@Nonnull Location location) {
        if (location.getWorld() != null) {
            Iterator var2 = location.getWorld().getNearbyEntities(location, 0.25, 0.25, 0.25).iterator();

            while(var2.hasNext()) {
                Entity entity = (Entity)var2.next();
                if (entity instanceof LivingEntity) {
                    LivingEntity livingEntity = (LivingEntity)entity;
                    livingEntity.damage((double)this.getDamage());
                }
            }

        }
    }
}
