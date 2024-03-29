package de.leon_lp9.challengePlugin.worldgeneration.worldGenerators;

import de.leon_lp9.challengePlugin.worldgeneration.populators.TreePopulator;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CustomChunkGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        // Generiere Terrain
        SimplexOctaveGenerator terrainGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        terrainGenerator.setScale(0.01D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = x + chunkX * 16;
                int realZ = z + chunkZ * 16;

                // Höhe berechnen
                int height = 100 + (int) (terrainGenerator.noise(realX, realZ, 0.5D, 0.5D) * 10D);


                //Noise für Biom [FOREST,DARK_FOREST,PLAINS]
                SimplexOctaveGenerator biomeGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 5);
                SimplexOctaveGenerator biomeGenerator2 = new SimplexOctaveGenerator(new Random(world.getSeed()), 4);
                biomeGenerator.setScale(0.006D);
                double biomeNoise = biomeGenerator.noise(realX, realZ, 0.5D, 0.5D) + biomeGenerator2.noise(realX, realZ, 0.5D, 0.5D) / 10;

                if (biomeNoise < -0.2) {
                    biome.setBiome(x, z, Biome.DARK_FOREST);
                } else if (biomeNoise > 0.2) {
                    biome.setBiome(x, z, Biome.FOREST);
                } else {
                    biome.setBiome(x, z, Biome.PLAINS);
                }


                // Blöcke setzen
                for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {
                    if (y < height - 4) {
                        if (y > 0) {
                            chunk.setBlock(x, y, z, Material.STONE);
                        }else {
                            chunk.setBlock(x, y, z, Material.DEEPSLATE);
                        }
                    } else if (y < height) {
                        if (y < height - 1) {
                            chunk.setBlock(x, y, z, Material.DIRT);
                        } else {
                            chunk.setBlock(x, y, z, Material.GRASS_BLOCK);
                        }

                        //wenn biome = Plains dann platziere auf dem Grass Block Weitzen
                        if (biome.getBiome(x, z) == Biome.PLAINS && y == height - 1) {
                            chunk.setBlock(x, y, z, new MaterialData(Material.FARMLAND));
                            chunk.setBlock(x, y+1, z, new MaterialData(Material.WHEAT, (byte)7));
                        }


                        //Grass Platzieren
                        SimplexOctaveGenerator grassGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 10);
                        grassGenerator.setScale(0.1D);
                        double grassNoise = grassGenerator.noise(realX, realZ, 0.5D, 0.5D);

                        if (grassNoise > 0.9) {
                            chunk.setBlock(x, y + 1, z, new MaterialData(Material.TALL_GRASS, (byte)2));
                            chunk.setBlock(x, y + 2, z, new MaterialData(Material.TALL_GRASS, (byte)0));
                        } else if (grassNoise > 0.6) {
                            chunk.setBlock(x, y + 1, z, Material.SHORT_GRASS);
                        }

                    }
                }

            }
        }

        return chunk;
    }

    @Override
    public boolean shouldGenerateCaves() {
        return false;
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
