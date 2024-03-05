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

import java.util.Optional;
import java.util.OptionalInt;
import java.util.function.BiPredicate;

import org.jetbrains.annotations.NotNull;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public interface BiomeContext {
	WeatherContext getWeather();

	EffectsContext getEffects();

	GenerationSettingsContext getGenerationSettings();

	SpawnSettingsContext getSpawnSettings();

	interface WeatherContext {

		void setPrecip(boolean hasPrecipitation);

		void setTemp(float temperature);

		void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier);

		void setDownfall(float downfall);
	}

	interface EffectsContext {

		void setFogColor(int color);

		void setWaterColor(int color);

		void setWaterFogColor(int color);

		void setSkyColor(int color);

		void setFoliageColor(Optional<Integer> color);

		default void setFoliageColor(int color) {
			setFoliageColor(Optional.of(color));
		}

		default void setFoliageColor(OptionalInt color) {
			color.ifPresentOrElse(this::setFoliageColor, this::clearFoliageColor);
		}

		default void clearFoliageColor() {
			setFoliageColor(Optional.empty());
		}

		void setGrassColor(Optional<Integer> color);

		default void setGrassColor(int color) {
			setGrassColor(Optional.of(color));
		}

		default void setGrassColor(OptionalInt color) {
			color.ifPresentOrElse(this::setGrassColor, this::clearGrassColor);
		}

		default void clearGrassColor() {
			setGrassColor(Optional.empty());
		}

		void setGrassColorModifier(@NotNull BiomeSpecialEffects.GrassColorModifier colorModifier);

		void setParticleConfig(Optional<AmbientParticleSettings> particleConfig);

		default void setParticleConfig(@NotNull AmbientParticleSettings particleConfig) {
			setParticleConfig(Optional.of(particleConfig));
		}

		default void clearParticleConfig() {
			setParticleConfig(Optional.empty());
		}

		void setAmbientSound(Optional<Holder<SoundEvent>> sound);

		default void setAmbientSound(@NotNull Holder<SoundEvent> sound) {
			setAmbientSound(Optional.of(sound));
		}

		default void clearAmbientSound() {
			setAmbientSound(Optional.empty());
		}

		void setMoodSound(Optional<AmbientMoodSettings> sound);

		default void setMoodSound(@NotNull AmbientMoodSettings sound) {
			setMoodSound(Optional.of(sound));
		}

		default void clearMoodSound() {
			setMoodSound(Optional.empty());
		}

		void setAdditionsSound(Optional<AmbientAdditionsSettings> sound);

		default void setAdditionsSound(@NotNull AmbientAdditionsSettings sound) {
			setAdditionsSound(Optional.of(sound));
		}

		default void clearAdditionsSound() {
			setAdditionsSound(Optional.empty());
		}

		void setMusic(Optional<Music> sound);

		default void setMusic(@NotNull Music sound) {
			setMusic(Optional.of(sound));
		}

		default void clearMusic() {
			setMusic(Optional.empty());
		}
	}

	interface GenerationSettingsContext {

		boolean removeFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey);

		default boolean removeFeature(ResourceKey<PlacedFeature> placedFeatureKey) {
			boolean anyFound = false;

			for (GenerationStep.Decoration step : GenerationStep.Decoration.values()) {
				if (removeFeature(step, placedFeatureKey)) {
					anyFound = true;
				}
			}

			return anyFound;
		}

		void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey);

		void addCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> carverKey);

		boolean removeCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> configuredCarverKey);

		default boolean removeCarver(ResourceKey<ConfiguredWorldCarver<?>> configuredCarverKey) {
			boolean anyFound = false;

			for (GenerationStep.Carving step : GenerationStep.Carving.values()) {
				if (removeCarver(step, configuredCarverKey)) {
					anyFound = true;
				}
			}

			return anyFound;
		}
	}

	interface SpawnSettingsContext {

		void setCreatureSpawnProbability(float probability);

		void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry);

		boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate);

		default boolean removeSpawnsOfEntityType(EntityType<?> entityType) {
			return removeSpawns((spawnGroup, spawnEntry) -> spawnEntry.type == entityType);
		}

		default void clearSpawns(MobCategory group) {
			removeSpawns((spawnGroup, spawnEntry) -> spawnGroup == group);
		}

		default void clearSpawns() {
			removeSpawns((spawnGroup, spawnEntry) -> true);
		}

		void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit);

		void clearSpawnCost(EntityType<?> entityType);
	}
}
