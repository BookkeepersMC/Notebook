package com.bookkeepersmc.notebook.common.world.impl;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

public class BuiltInKeys {
    private static final HolderLookup.Provider registries = VanillaRegistries.createLookup();

    private BuiltInKeys() {
    }

    public static boolean isBuiltinBiome(ResourceKey<Biome> key) {
        return biomeRegistryWrapper().get(key).isPresent();
    }

    public static HolderGetter<Biome> biomeRegistryWrapper() {
        return registries.lookupOrThrow(Registries.BIOME);
    }
}
