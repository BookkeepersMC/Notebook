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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.google.common.base.Preconditions;
import com.mojang.datafixers.util.Pair;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.MultiNoiseBiomeSourceParameterList;

public final class NetherData {
	private static final Set<ResourceKey<Biome>> NETHER_BIOMES = new HashSet<>();

	private static final Map<ResourceKey<Biome>, Climate.ParameterPoint> NETHER_BIOME_NOISE_POINTS = new HashMap<>();

	private NetherData() {
	}

	public static void addNetherBiome(ResourceKey<Biome> biome, Climate.ParameterPoint spawnNoisePoint) {
		Preconditions.checkArgument(biome != null, "Biome is null");
		Preconditions.checkArgument(spawnNoisePoint != null, "MultiNoiseBiomeSourceParameterList is null");
		NETHER_BIOME_NOISE_POINTS.put(biome, spawnNoisePoint);
		clearBiomeSourceCache();
	}

	public static boolean canGenerateInNether(ResourceKey<Biome> biome) {
		return MultiNoiseBiomeSourceParameterList.Preset.NETHER.usedBiomes().anyMatch(input -> input.equals(biome));
	}

	private static void clearBiomeSourceCache() {
		NETHER_BIOMES.clear();
	}

	public static <T> Climate.ParameterList<T> withModdedBiomeEntries(Climate.ParameterList<T> entries, Function<ResourceKey<Biome>, T> biomes) {
		if (NETHER_BIOME_NOISE_POINTS.isEmpty()) {
			return entries;
		}

		ArrayList<Pair<Climate.ParameterPoint, T>> entryList = new ArrayList<>(entries.values());

		for (Map.Entry<ResourceKey<Biome>, Climate.ParameterPoint> entry : NETHER_BIOME_NOISE_POINTS.entrySet()) {
			entryList.add(Pair.of(entry.getValue(), biomes.apply(entry.getKey())));
		}

		return new Climate.ParameterList<>(Collections.unmodifiableList(entryList));
	}
}
