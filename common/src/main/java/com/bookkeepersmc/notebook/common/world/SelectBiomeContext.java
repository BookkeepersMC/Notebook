/*
 * Copyright (c) 2023, 2024 BookkeepersMC under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

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
