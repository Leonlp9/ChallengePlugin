package de.leon_lp9.challengePlugin.worldgeneration.populators;

import org.bukkit.Chunk;
import org.bukkit.TreeType;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

public class TreePopulator extends BlockPopulator {

    @Override
    public void populate(World world, Random random, Chunk chunk) {
        // Wir wollen nicht in jedem Chunk Bäume generieren, daher können wir hier eine Wahrscheinlichkeit festlegen
        if (random.nextFloat() < 0.3f) {
            int chunkX = chunk.getX() << 4; // Bit-Shifting, entspricht chunk.getX() * 16
            int chunkZ = chunk.getZ() << 4; // Bit-Shifting, entspricht chunk.getZ() * 16

            // Wähle eine zufällige Position innerhalb des Chunks
            int x = chunkX + random.nextInt(16);
            int z = chunkZ + random.nextInt(16);

            // Bestimme die Höhe an der gewählten Position
            int y = world.getHighestBlockYAt(x, z);

            // Erzeuge einen Baum an dieser Position
            world.generateTree(world.getBlockAt(x, y, z).getLocation(), TreeType.BIG_TREE);
        }
    }
}