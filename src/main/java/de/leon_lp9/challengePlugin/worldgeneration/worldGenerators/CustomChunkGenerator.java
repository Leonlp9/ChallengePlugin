package de.leon_lp9.challengePlugin.worldgeneration.worldGenerators;

import de.leon_lp9.challengePlugin.worldgeneration.populators.TreePopulator;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {

    // Wasserlevel
    private static final int WATER_LEVEL = 60;

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        // Generiere Terrain
        SimplexOctaveGenerator terrainGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        terrainGenerator.setScale(0.01D);

        SimplexOctaveGenerator beachGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 6);
        beachGenerator.setScale(0.03D);

        SimplexOctaveGenerator mountainGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 10);
        mountainGenerator.setScale(0.02D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = x + chunkX * 16;
                int realZ = z + chunkZ * 16;

                // Höhe berechnen
                int height = WATER_LEVEL + (int) (terrainGenerator.noise(realX, realZ, 0.5D, 0.5D) * 40D +
                        beachGenerator.noise(realX, realZ, 0.5D, 0.5D) * 10D +
                        mountainGenerator.noise(realX, realZ, 0.5D, 0.5D) * 30D);

                // Biome setzen
                biome.setBiome(x, z, height > WATER_LEVEL ? Biome.PLAINS : Biome.OCEAN);

                // Blöcke setzen
                for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                    if (y < height - 4) {
                        chunk.setBlock(x, y, z, Material.STONE);
                    } else if (y < height) {
                        chunk.setBlock(x, y, z, Material.GRASS_BLOCK);

                    } else if (y <= WATER_LEVEL) {
                        chunk.setBlock(x, y, z, Material.WATER);
                    }
                }
            }
        }

        return chunk;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return true;
    }

    @Override
    public boolean shouldGenerateStructures() {
        return true;
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        List<BlockPopulator> populators = new ArrayList<>();
        populators.add(new TreePopulator());
        return populators;
    }


}
