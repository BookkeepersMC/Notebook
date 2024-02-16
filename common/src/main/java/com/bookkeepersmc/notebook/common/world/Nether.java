package com.bookkeepersmc.notebook.common.world;

import com.bookkeepersmc.notebook.common.world.impl.NetherData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

public final class Nether {
    private Nether() {
    }

    public static void addNetherBiome(ResourceKey<Biome> biome, Climate.TargetPoint mixedNoisePoint) {
        NetherData.addNetherBiome(biome, Climate.parameters(
                mixedNoisePoint.temperature(),
                mixedNoisePoint.humidity(),
                mixedNoisePoint.continentalness(),
                mixedNoisePoint.erosion(),
                mixedNoisePoint.depth(),
                mixedNoisePoint.weirdness(),
                0
        ));
    }

    public static void addNetherBiome(ResourceKey<Biome> biome, Climate.ParameterPoint mixedNoisePoint) {
        NetherData.addNetherBiome(biome, mixedNoisePoint);
    }

    public static boolean canGenerateInNether(ResourceKey<Biome> biome) {
        return NetherData.canGenerateInNether(biome);
    }
}