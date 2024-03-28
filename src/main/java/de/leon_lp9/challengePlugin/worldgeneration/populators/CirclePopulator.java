package de.leon_lp9.challengePlugin.worldgeneration.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class CirclePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {

        //Entferne alle Blöcke die nicht auf dem Kreis umriss liegen

        double radius = 7.5;
        int centerX = chunk.getBlock(8, 0, 8).getX();
        int centerZ = chunk.getBlock(8, 0, 8).getZ();

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = world.getMinHeight(); k < world.getMaxHeight(); k++) {

                    double distance = Math.sqrt(Math.pow(centerX - chunk.getBlock(i, k, j).getX(), 2) + Math.pow(centerZ - chunk.getBlock(i, k, j).getZ(), 2));

                    //lösche alle blöcke die nicht auf dem kreis liegen. Achtung: Nicht außerhalb des Chunks löschen da sonst die Welt nicht generiert wird
                    if (distance > radius || distance < radius - 1 || i == 0 || j == 0) {
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

        //Entferne alle Entities die nicht auf dem Kreis liegen
        for (Entity entity : chunk.getEntities()) {
            double distance = Math.sqrt(Math.pow(centerX - entity.getLocation().getBlockX(), 2) + Math.pow(centerZ - entity.getLocation().getBlockZ(), 2));
            if (distance > radius || distance < radius - 1) {
                if (entity.getType() == EntityType.ENDER_DRAGON){
                    continue;
                }
                entity.remove();
            }
        }

    }

}
