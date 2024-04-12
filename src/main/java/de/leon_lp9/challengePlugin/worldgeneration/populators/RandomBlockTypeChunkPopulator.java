package de.leon_lp9.challengePlugin.worldgeneration.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomBlockTypeChunkPopulator extends BlockPopulator {

    @Override
    public void populate(@NotNull World world, @NotNull Random random, @NotNull Chunk source) {

        Material randomMaterial = Material.values()[random.nextInt(Material.values().length)];

        for (int i = 0; i < 16; i++) {
            for (int j = 0; j < 16; j++) {
                for (int k = world.getMinHeight(); k < world.getMaxHeight(); k++) {
                    source.getBlock(i, k, j).setType(randomMaterial);
                }
            }

        }
    }
}
