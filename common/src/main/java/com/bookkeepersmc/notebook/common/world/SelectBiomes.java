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

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import com.google.common.collect.ImmutableSet;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;

import com.bookkeepersmc.notebook.common.world.impl.BuiltInKeys;

/**
 * Provides several biome selectors that can be used with {@link BiomeRegistry}.
 */
public final class SelectBiomes {
	private SelectBiomes() {
	}
	public static Predicate<SelectBiomeContext> all() {
		return context -> true;
	}

	public static Predicate<SelectBiomeContext> vanilla() {
		return context -> {
			return context.getBiomeKey().location().getNamespace().equals("minecraft")
					&& BuiltInKeys.isBuiltinBiome(context.getBiomeKey());
		};
	}

	public static Predicate<SelectBiomeContext> foundInOverworld() {
		return context -> context.canGenerateIn(LevelStem.OVERWORLD);
	}

	public static Predicate<SelectBiomeContext> foundInTheNether() {
		return context -> context.canGenerateIn(LevelStem.NETHER);
	}

	public static Predicate<SelectBiomeContext> foundInTheEnd() {
		return context -> context.canGenerateIn(LevelStem.END);
	}

	public static Predicate<SelectBiomeContext> tag(TagKey<Biome> tag) {
		return context -> context.hasTag(tag);
	}

	@SafeVarargs
	public static Predicate<SelectBiomeContext> excludeByKey(ResourceKey<Biome>... keys) {
		return excludeByKey(ImmutableSet.copyOf(keys));
	}

	public static Predicate<SelectBiomeContext> excludeByKey(Collection<ResourceKey<Biome>> keys) {
		return context -> !keys.contains(context.getBiomeKey());
	}

	@SafeVarargs
	public static Predicate<SelectBiomeContext> includeByKey(ResourceKey<Biome>... keys) {
		return includeByKey(ImmutableSet.copyOf(keys));
	}

	public static Predicate<SelectBiomeContext> includeByKey(Collection<ResourceKey<Biome>> keys) {
		return context -> keys.contains(context.getBiomeKey());
	}

	public static Predicate<SelectBiomeContext> spawnsOneOf(EntityType<?>... entityTypes) {
		return spawnsOneOf(ImmutableSet.copyOf(entityTypes));
	}

	public static Predicate<SelectBiomeContext> spawnsOneOf(Set<EntityType<?>> entityTypes) {
		return context -> {
			MobSpawnSettings spawnSettings = context.getBiome().getMobSettings();

			for (MobCategory spawnGroup : MobCategory.values()) {
				for (MobSpawnSettings.SpawnerData spawnEntry : spawnSettings.getMobs(spawnGroup).unwrap()) {
					if (entityTypes.contains(spawnEntry.type)) {
						return true;
					}
				}
			}

			return false;
		};
	}
}
