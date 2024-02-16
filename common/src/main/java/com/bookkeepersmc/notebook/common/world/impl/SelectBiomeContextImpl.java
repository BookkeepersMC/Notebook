package com.bookkeepersmc.notebook.common.world.impl;

import java.util.Optional;

import com.bookkeepersmc.notebook.common.world.SelectBiomeContext;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public class SelectBiomeContextImpl implements SelectBiomeContext {
    private final RegistryAccess dynamicRegistries;
    private final ResourceKey<Biome> key;
    private final Biome biome;
    private final Holder<Biome> entry;

    public SelectBiomeContextImpl(RegistryAccess dynamicRegistries, ResourceKey<Biome> key, Biome biome) {
        this.dynamicRegistries = dynamicRegistries;
        this.key = key;
        this.biome = biome;
        this.entry = dynamicRegistries.registryOrThrow(Registries.BIOME).getHolder(this.key).orElseThrow();
    }

    @Override
    public ResourceKey<Biome> getBiomeKey() {
        return key;
    }

    @Override
    public Biome getBiome() {
        return biome;
    }

    @Override
    public Holder<Biome> getBiomeRegistryEntry() {
        return entry;
    }

    @Override
    public Optional<ResourceKey<ConfiguredFeature<?, ?>>> getFeature(ConfiguredFeature<?, ?> configuredFeature) {
        Registry<ConfiguredFeature<?, ?>> registry = dynamicRegistries.registryOrThrow(Registries.CONFIGURED_FEATURE);
        return registry.getResourceKey(configuredFeature);
    }

    @Override
    public Optional<ResourceKey<PlacedFeature>> getPlacedFeature(PlacedFeature placedFeature) {
        Registry<PlacedFeature> registry = dynamicRegistries.registryOrThrow(Registries.PLACED_FEATURE);
        return registry.getResourceKey(placedFeature);
    }

    @Override
    public boolean validForStructure(ResourceKey<Structure> key) {
        Structure instance = dynamicRegistries.registryOrThrow(Registries.STRUCTURE).get(key);

        if (instance == null) {
            return false;
        }

        return instance.biomes().contains(getBiomeRegistryEntry());
    }

    @Override
    public Optional<ResourceKey<Structure>> getStructureKey(Structure structure) {
        Registry<Structure> registry = dynamicRegistries.registryOrThrow(Registries.STRUCTURE);
        return registry.getResourceKey(structure);
    }

    @Override
    public boolean canGenerateIn(ResourceKey<LevelStem> dimensionKey) {
        LevelStem dimension = dynamicRegistries.registryOrThrow(Registries.LEVEL_STEM).get(dimensionKey);

        if (dimension == null) {
            return false;
        }

        return dimension.generator().getBiomeSource().possibleBiomes().stream().anyMatch(entry -> entry.value() == biome);
    }

    @Override
    public boolean hasTag(TagKey<Biome> tag) {
        Registry<Biome> biomeRegistry = dynamicRegistries.registryOrThrow(Registries.BIOME);
        return biomeRegistry.getHolderOrThrow(getBiomeKey()).is(tag);
    }
}
