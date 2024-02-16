package com.bookkeepersmc.notebook.common.world;

import com.bookkeepersmc.notebook.common.world.impl.TheEndData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

public final class TheEnd {
    private TheEnd() {
    }

    public static void addMainIslandBiome(ResourceKey<Biome> biome, double weight) {
        TheEndData.addEndBiomeReplacement(Biomes.THE_END, biome, weight);
    }

    public static void addHighlandsBiome(ResourceKey<Biome> biome, double weight) {
        TheEndData.addEndBiomeReplacement(Biomes.END_HIGHLANDS, biome, weight);
    }

    public static void addSmallIslandsBiome(ResourceKey<Biome> biome, double weight) {
        TheEndData.addEndBiomeReplacement(Biomes.SMALL_END_ISLANDS, biome, weight);
    }

    public static void addMidlandsBiome(ResourceKey<Biome> highlands, ResourceKey<Biome> midlands, double weight) {
        TheEndData.addEndMidlandsReplacement(highlands, midlands, weight);
    }

    public static void addBarrensBiome(ResourceKey<Biome> highlands, ResourceKey<Biome> barrens, double weight) {
        TheEndData.addEndBarrensReplacement(highlands, barrens, weight);
    }
}
