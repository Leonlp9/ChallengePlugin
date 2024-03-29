package de.leon_lp9.challengePlugin.worldgeneration.biomeProvider;

import org.apache.commons.lang3.Validate;
import org.bukkit.block.Biome;
import org.bukkit.generator.BiomeProvider;
import org.bukkit.generator.WorldInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class SingleBiomeProvider extends BiomeProvider {

    @NotNull
    private final Biome biome;

    public SingleBiomeProvider(@NotNull Biome biome) {
        Validate.notNull(biome, "Biome cannot be null");

        this.biome = biome;
    }

    @NotNull
    @Override
    public Biome getBiome(@NotNull WorldInfo worldInfo, int x, int y, int z) {
        return biome;
    }

    @NotNull
    @Override
    public List<Biome> getBiomes(@NotNull WorldInfo worldInfo) {
        return Collections.singletonList(biome);
    }
}