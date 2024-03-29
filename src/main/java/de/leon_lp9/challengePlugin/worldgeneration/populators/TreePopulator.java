package de.leon_lp9.challengePlugin.worldgeneration.populators;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.TreeType;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.LimitedRegion;
import org.bukkit.generator.WorldInfo;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TreePopulator extends BlockPopulator {
    private final HashMap<Biome, List<TreeType>> biomeTrees = new HashMap<Biome, List<TreeType>>() {{
        put(Biome.PLAINS, Arrays.asList());
        put(Biome.FOREST, Arrays.asList(TreeType.BIRCH));
        put(Biome.DARK_FOREST, Arrays.asList(TreeType.DARK_OAK));
    }};

    private final int minTreeDistance = 2; // Mindestabstand zwischen Bäumen (in Blöcken)

    @Override
    public void populate(WorldInfo worldInfo, Random random, int chunkX, int chunkZ, LimitedRegion limitedRegion) {
        //erstelle eine hightmap
        int[][] heightMap = new int[16][16];
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                heightMap[x][z] = limitedRegion.getHighestBlockYAt(x + chunkX * 16, z + chunkZ * 16);
            }
        }

        for (int i = 0; i < 5; i++) { // Anzahl der Bäume im Chunk anpassen
            int x = random.nextInt(16) + chunkX * 16;
            int z = random.nextInt(16) + chunkZ * 16;
            int y = heightMap[x & 15][z & 15];

            Location location = new Location(Bukkit.getWorld(worldInfo.getUID()), x, y, z);
            List<TreeType> trees = biomeTrees.getOrDefault(limitedRegion.getBiome(location), Arrays.asList());

            //wenn es kein blatt ist
            if (!trees.isEmpty() && limitedRegion.getType(x, y - 1, z).isSolid() && !limitedRegion.getType(x, y - 1, z).name().contains("LEAVES")) {
                limitedRegion.generateTree(location, random, trees.get(random.nextInt(trees.size())));
            }
        }
    }
}