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
package com.bookkeepersmc.notebook.common.world.impl;

import java.util.Optional;

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

import com.bookkeepersmc.notebook.common.world.SelectBiomeContext;

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
