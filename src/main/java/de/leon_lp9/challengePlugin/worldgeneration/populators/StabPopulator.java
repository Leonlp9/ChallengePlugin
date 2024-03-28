package de.leon_lp9.challengePlugin.worldgeneration.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class StabPopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = world.getMinHeight(); k < world.getMaxHeight(); k++) {
                    if (i!=0 || j!=0) {
                        Material material = chunk.getBlock(i, k, j).getType();

                        if (material == Material.END_PORTAL_FRAME || material == Material.END_PORTAL || material == Material.END_GATEWAY) {
                            continue;
                        }

                        if (material != Material.AIR) {
                            chunk.getBlock(i, k, j).setBlockData(Material.AIR.createBlockData(), false);
                        }
                    }
                }
            }
        }

        //remove entities
        for (Entity entity : chunk.getEntities()) {
            int x = entity.getLocation().getBlockX();
            int z = entity.getLocation().getBlockZ();
            if (x != z && x + 1 != z && (x != 15 || z != 0)) {
                if (entity.getType() == EntityType.ENDER_DRAGON){
                    continue;
                }
                entity.remove();
            }
        }

    }

}
