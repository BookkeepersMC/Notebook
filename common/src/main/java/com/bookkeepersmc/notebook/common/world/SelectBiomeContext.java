package com.bookkeepersmc.notebook.common.world;

import java.util.List;
import java.util.Optional;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.Structure;

public interface SelectBiomeContext {
    ResourceKey<Biome> getBiomeKey();

    Biome getBiome();

    Holder<Biome> getBiomeRegistryEntry();

    default boolean hasFeature(ResourceKey<ConfiguredFeature<?, ?>> key) {
        List<HolderSet<PlacedFeature>> featureSteps = getBiome().getGenerationSettings().features();

        for (HolderSet<PlacedFeature> featureSuppliers : featureSteps) {
            for (Holder<PlacedFeature> featureSupplier : featureSuppliers) {
                if (featureSupplier.value().getFeatures().anyMatch(cf -> getFeature(cf).orElse(null) == key)) {
                    return true;
                }
            }
        }

        return false;
    }

    default boolean hasPlacedFeature(ResourceKey<PlacedFeature> key) {
        List<HolderSet<PlacedFeature>> featureSteps = getBiome().getGenerationSettings().features();

        for (HolderSet<PlacedFeature> featureSuppliers : featureSteps) {
            for (Holder<PlacedFeature> featureSupplier : featureSuppliers) {
                if (getPlacedFeature(featureSupplier.value()).orElse(null) == key) {
                    return true;
                }
            }
        }

        return false;
    }

    Optional<ResourceKey<ConfiguredFeature<?, ?>>> getFeature(ConfiguredFeature<?, ?> configuredFeature);

    Optional<ResourceKey<PlacedFeature>> getPlacedFeature(PlacedFeature placedFeature);

    boolean validForStructure(ResourceKey<Structure> key);

    Optional<ResourceKey<Structure>> getStructureKey(Structure structureFeature);

    boolean canGenerateIn(ResourceKey<LevelStem> dimensionKey);

    boolean hasTag(TagKey<Biome> tag);
}
