package de.leon_lp9.challengePlugin.worldgeneration.worldGenerators;

import de.leon_lp9.challengePlugin.worldgeneration.populators.TreePopulator;
import org.bukkit.GrassSpecies;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.LongGrass;
import org.bukkit.material.MaterialData;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestGenerator extends ChunkGenerator {

    @Override
    public ChunkData generateChunkData(World world, Random random, int chunkX, int chunkZ, BiomeGrid biome) {
        ChunkData chunk = createChunkData(world);

        // Generiere Terrain
        SimplexOctaveGenerator terrainGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 8);
        terrainGenerator.setScale(0.001D);

        //second generator
        SimplexOctaveGenerator terrainGenerator2 = new SimplexOctaveGenerator(new Random(world.getSeed()), 7);
        terrainGenerator2.setScale(0.005D);

        //second generator
        SimplexOctaveGenerator terrainGenerator3 = new SimplexOctaveGenerator(new Random(world.getSeed()), 6);
        terrainGenerator3.setScale(0.005D);

        //third generator
        SimplexOctaveGenerator terrainGenerator4 = new SimplexOctaveGenerator(new Random(world.getSeed()), 5);
        terrainGenerator4.setScale(0.035D);

        //fourth generator
        SimplexOctaveGenerator terrainGenerator5 = new SimplexOctaveGenerator(new Random(world.getSeed()), 10);
        terrainGenerator5.setScale(0.009D);

        //fifth generator
        SimplexOctaveGenerator terrainGenerator6 = new SimplexOctaveGenerator(new Random(world.getSeed()), 10);
        terrainGenerator6.setScale(0.1D);

        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int realX = x + chunkX * 16;
                int realZ = z + chunkZ * 16;

                // Höhe berechnen
                int height = 100 + (int)( (terrainGenerator.noise(realX, realZ, 0.5D, 0.5D) * 7D) + (terrainGenerator2.noise(realX, realZ, 0.5D, 0.5D) * 3D));

                int abolute = 0;
                double noise = (terrainGenerator3.noise(realX, realZ, 0.5D, 0.5D) + terrainGenerator4.noise(realX, realZ, 0.5D, 0.5D) / 10) / 2 + terrainGenerator6.noise(realX, realZ, 0.5D, 0.5D) / 40;
                if (noise > 0.27) {
                    height += 1;
                    abolute += 1;
                }if (noise > 0.275) {
                    height += 2;
                    abolute += 1;
                }if (noise > 0.28) {
                    height += 3;
                    abolute += 1;
                }if (noise > 0.285) {
                    height += 2;
                    abolute += 1;
                }if (noise > 0.29) {
                    height += 1;
                    abolute += 1;
                }

                boolean generateStone = noise >= 0.27 && noise < 0.3 && abolute < 5;

                List<Material> materials = new ArrayList<>();
                materials.add(Material.STONE);
                materials.add(Material.ANDESITE);
                materials.add(Material.COBBLESTONE);
                materials.add(Material.STONE);
                materials.add(Material.ANDESITE);
                materials.add(Material.COBBLESTONE);
                materials.add(Material.TUFF);


                biome.setBiome(x, z, Biome.JUNGLE);


                // Blöcke setzen
                for (int y = world.getMinHeight(); y < world.getMaxHeight(); y++) {


                    if (y < height - 4) {
                        if (y > 0) {
                            chunk.setBlock(x, y, z, Material.STONE);
                        }else {
                            chunk.setBlock(x, y, z, Material.DEEPSLATE);
                        }
                    } else if (y < height) {
                        if (!generateStone) {
                            if (y < height - 1) {
                                if (abolute > 0)
                                    chunk.setBlock(x, y, z, materials.get(random.nextInt(materials.size())));
                                else
                                    chunk.setBlock(x, y, z, Material.DIRT);
                            } else {
                                chunk.setBlock(x, y, z, Material.GRASS_BLOCK);
                            }
                        }else {
                            chunk.setBlock(x, y, z, materials.get(random.nextInt(materials.size())));
                        }

                        if (y == height - 1) {
                            if ((terrainGenerator5.noise(realX, realZ , 0.5D, 0.5D) + terrainGenerator6.noise(realX, realZ, 0.5D, 0.5D)) > 1.275 && noise < 0.275) {
                                chunk.setBlock(x, y, z, new MaterialData(Material.FARMLAND));
                                chunk.setBlock(x, y+1, z, new MaterialData(Material.WHEAT, (byte)7));
                            }else {
                                //Grass Platzieren
                                SimplexOctaveGenerator grassGenerator = new SimplexOctaveGenerator(new Random(world.getSeed()), 10);
                                grassGenerator.setScale(0.1D);
                                double grassNoise = grassGenerator.noise(realX, realZ, 0.5D, 0.5D);

                                if (!generateStone) {
                                    if (grassNoise > 0.8) {

                                        //lower half
                                        chunk.setBlock(x, y + 1, z, new MaterialData(Material.TALL_GRASS, (byte) 2));

                                        //upper half

                                        BlockData blockData = Material.TALL_GRASS.createBlockData();
                                        if (blockData instanceof Bisected) {
                                            ((Bisected) blockData).setHalf(Bisected.Half.TOP);
                                        }
                                        chunk.setBlock(x, y + 2, z, blockData);


                                    } else if (grassNoise > 0.5) {
                                        chunk.setBlock(x, y + 1, z, Material.SHORT_GRASS);
                                    }
                                }
                            }

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
        return populators;
    }


}
