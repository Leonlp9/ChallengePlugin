package de.leon_lp9.challengePlugin.worldgeneration.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class SpherePopulator extends BlockPopulator {

    //jeder chunk ist eine platte. Nur eine zufällige y-ebene pro chunk wird generiert

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        int miny = world.getMinHeight();
        int maxy = world.getMaxHeight();

        int highestBlockY = maxy;
        while (chunk.getBlock(8, highestBlockY, 8).getType() == Material.AIR) {
            highestBlockY--;
        }

        int y = random.nextInt(highestBlockY - miny) + miny;

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = miny; k < maxy; k++) {
                    if (!(Math.pow(i - 8, 2) + Math.pow(j - 8, 2) + Math.pow(k - y, 2) > 7.5 * 7.5)) {
                        continue;
                    }

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

        //remove entities
        for (Entity entity : chunk.getEntities()) {
            if (!(Math.pow(entity.getLocation().getBlockX() - 8, 2) + Math.pow(entity.getLocation().getBlockZ() - 8, 2) + Math.pow(entity.getLocation().getBlockY() - y, 2) > 7.5 * 7.5)) {
                if (entity.getType() == EntityType.ENDER_DRAGON){
                    continue;
                }
                entity.remove();
            }
        }

    }

}
