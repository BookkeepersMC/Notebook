package com.bookkeepersmc.notebook.common.world.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

import com.bookkeepersmc.notebook.common.world.BiomeContext;
import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.AmbientAdditionsSettings;
import net.minecraft.world.level.biome.AmbientMoodSettings;
import net.minecraft.world.level.biome.AmbientParticleSettings;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeContextImpl implements BiomeContext {
    private final RegistryAccess registries;
    private final Biome biome;
    private final WeatherContext weather;
    private final EffectsContext effects;
    private final GenerationSettingsContextImpl generationSettings;
    private final SpawnSettingsContextImpl spawnSettings;

    public BiomeContextImpl(RegistryAccess registries, Biome biome) {
        this.registries = registries;
        this.biome = biome;
        this.weather = new WeatherContextImpl();
        this.effects = new EffectsContextImpl();
        this.generationSettings = new GenerationSettingsContextImpl();
        this.spawnSettings = new SpawnSettingsContextImpl();
    }

    @Override
    public WeatherContext getWeather() {
        return weather;
    }

    @Override
    public EffectsContext getEffects() {
        return effects;
    }

    @Override
    public GenerationSettingsContext getGenerationSettings() {
        return generationSettings;
    }

    @Override
    public SpawnSettingsContext getSpawnSettings() {
        return spawnSettings;
    }
    void freeze() {
        generationSettings.freeze();
        spawnSettings.freeze();
    }

    private class WeatherContextImpl implements WeatherContext {
        @Override
        public void setPrecip(boolean hasPrecipitation) {
            biome.climateSettings = new Biome.ClimateSettings(hasPrecipitation, biome.climateSettings.temperature(), biome.climateSettings.temperatureModifier(), biome.climateSettings.downfall());
        }

        @Override
        public void setTemp(float temperature) {
            biome.climateSettings = new Biome.ClimateSettings(biome.climateSettings.hasPrecipitation(), temperature, biome.climateSettings.temperatureModifier(), biome.climateSettings.downfall());
        }

        @Override
        public void setTemperatureModifier(Biome.TemperatureModifier temperatureModifier) {
            biome.climateSettings = new Biome.ClimateSettings(biome.climateSettings.hasPrecipitation(), biome.climateSettings.temperature(), Objects.requireNonNull(temperatureModifier), biome.climateSettings.downfall());
        }

        @Override
        public void setDownfall(float downfall) {
            biome.climateSettings = new Biome.ClimateSettings(biome.climateSettings.hasPrecipitation(), biome.climateSettings.temperature(), biome.climateSettings.temperatureModifier(), downfall);
        }
    }

    private class EffectsContextImpl implements EffectsContext {
        private final BiomeSpecialEffects effects = biome.getSpecialEffects();

        @Override
        public void setFogColor(int color) {
            effects.fogColor = color;
        }

        @Override
        public void setWaterColor(int color) {
            effects.waterColor = color;
        }

        @Override
        public void setWaterFogColor(int color) {
            effects.waterFogColor = color;
        }

        @Override
        public void setSkyColor(int color) {
            effects.skyColor = color;
        }

        @Override
        public void setFoliageColor(Optional<Integer> color) {
            effects.foliageColorOverride = Objects.requireNonNull(color);
        }

        @Override
        public void setGrassColor(Optional<Integer> color) {
            effects.grassColorOverride = Objects.requireNonNull(color);
        }

        @Override
        public void setGrassColorModifier(@NotNull BiomeSpecialEffects.GrassColorModifier colorModifier) {
            effects.grassColorModifier = Objects.requireNonNull(colorModifier);
        }

        @Override
        public void setParticleConfig(Optional<AmbientParticleSettings> particleConfig) {
            effects.ambientParticleSettings = Objects.requireNonNull(particleConfig);
        }

        @Override
        public void setAmbientSound(Optional<Holder<SoundEvent>> sound) {
            effects.ambientLoopSoundEvent = Objects.requireNonNull(sound);
        }

        @Override
        public void setMoodSound(Optional<AmbientMoodSettings> sound) {
            effects.ambientMoodSettings = Objects.requireNonNull(sound);
        }

        @Override
        public void setAdditionsSound(Optional<AmbientAdditionsSettings> sound) {
            effects.ambientAdditionsSettings = Objects.requireNonNull(sound);
        }

        @Override
        public void setMusic(Optional<Music> sound) {
            effects.backgroundMusic = Objects.requireNonNull(sound);
        }
    }

    private class GenerationSettingsContextImpl implements GenerationSettingsContext {
        private final Registry<ConfiguredWorldCarver<?>> carvers = registries.registryOrThrow(Registries.CONFIGURED_CARVER);
        private final Registry<PlacedFeature> features = registries.registryOrThrow(Registries.PLACED_FEATURE);
        private final BiomeGenerationSettings generationSettings = biome.getGenerationSettings();

        private boolean rebuildFlowerFeatures;

        GenerationSettingsContextImpl() {
            unfreezeCarvers();
            unfreezeFeatures();

            rebuildFlowerFeatures = false;
        }

        private void unfreezeCarvers() {
            Map<GenerationStep.Carving, HolderSet<ConfiguredWorldCarver<?>>> carversByStep = new EnumMap<>(GenerationStep.Carving.class);
            carversByStep.putAll(generationSettings.carvers);

            generationSettings.carvers = carversByStep;
        }

        private void unfreezeFeatures() {
            generationSettings.features = new ArrayList<>(generationSettings.features);
        }

        public void freeze() {
            freezeCarvers();
            freezeFeatures();

            if (rebuildFlowerFeatures) {
                rebuildFlowerFeatures();
            }
        }

        private void freezeCarvers() {
            generationSettings.carvers = ImmutableMap.copyOf(generationSettings.carvers);
        }

        private void freezeFeatures() {
            generationSettings.features = ImmutableList.copyOf(generationSettings.features);
            generationSettings.featureSet = Suppliers.memoize(() -> {
                return generationSettings.features.stream().flatMap(HolderSet::stream).map(Holder::value).collect(Collectors.toSet());
            });
        }

        private void rebuildFlowerFeatures() {
            generationSettings.flowerFeatures = Suppliers.memoize(() -> {
                return generationSettings.features.stream().flatMap(HolderSet::stream).map(Holder::value).flatMap(PlacedFeature::getFeatures).filter((configuredFeature) -> {
                    return configuredFeature.feature() == Feature.FLOWER;
                }).collect(ImmutableList.toImmutableList());
            });
        }

        @Override
        public boolean removeFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureKey) {
            PlacedFeature placedFeature = getEntry(features, placedFeatureKey).value();

            int stepIndex = step.ordinal();
            List<HolderSet<PlacedFeature>> featureSteps = generationSettings.features;

            if (stepIndex >= featureSteps.size()) {
            }

            HolderSet<PlacedFeature> featuresInStep = featureSteps.get(stepIndex);
            List<Holder<PlacedFeature>> features = new ArrayList<>(featuresInStep.stream().toList());

            if (features.removeIf(feature -> feature.value() == placedFeature)) {
                featureSteps.set(stepIndex, HolderSet.direct(features));
                rebuildFlowerFeatures = true;

                return true;
            }

            return false;
        }

        @Override
        public void addFeature(GenerationStep.Decoration step, ResourceKey<PlacedFeature> entry) {
            List<HolderSet<PlacedFeature>> featureSteps = generationSettings.features;
            int index = step.ordinal();

            while (index >= featureSteps.size()) {
                featureSteps.add(HolderSet.direct(Collections.emptyList()));
            }

            featureSteps.set(index, plus(featureSteps.get(index), getEntry(features, entry)));

            rebuildFlowerFeatures = true;
        }

        @Override
        public void addCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> entry) {
            generationSettings.carvers.put(step, plus(generationSettings.carvers.get(step), getEntry(carvers, entry)));
        }

        @Override
        public boolean removeCarver(GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> configuredCarverKey) {
            ConfiguredWorldCarver<?> carver = getEntry(carvers, configuredCarverKey).value();
            HolderSet<ConfiguredWorldCarver<?>> carvers = generationSettings.carvers.get(step);

            if (carvers == null) return false;

            List<Holder<ConfiguredWorldCarver<?>>> genCarvers = new ArrayList<>(carvers.stream().toList());

            if (genCarvers.removeIf(entry -> entry.value() == carver)) {
                generationSettings.carvers.put(step, HolderSet.direct(genCarvers));
                return true;
            }

            return false;
        }

        private <T> HolderSet<T> plus(@Nullable HolderSet<T> values, Holder<T> entry) {
            if (values == null) return HolderSet.direct(entry);

            List<Holder<T>> list = new ArrayList<>(values.stream().toList());
            list.add(entry);
            return HolderSet.direct(list);
        }
    }

    private static <T> Holder.Reference<T> getEntry(Registry<T> registry, ResourceKey<T> key) {
        Holder.Reference<T> entry = registry.getHolder(key).orElse(null);

        if (entry == null) {
            throw new IllegalArgumentException("Couldn't find registry entry for " + key);
        }

        return entry;
    }

    private class SpawnSettingsContextImpl implements SpawnSettingsContext {
        private final MobSpawnSettings spawnSettings = biome.getMobSettings();
        private final EnumMap<MobCategory, List<MobSpawnSettings.SpawnerData>> spawners = new EnumMap<>(MobCategory.class);

        SpawnSettingsContextImpl() {
            unfreezeSpawners();
            unfreezeSpawnCost();
        }

        private void unfreezeSpawners() {
            spawners.clear();

            for (MobCategory spawnGroup : MobCategory.values()) {
                WeightedRandomList<MobSpawnSettings.SpawnerData> entries = spawnSettings.spawners.get(spawnGroup);

                if (entries != null) {
                    spawners.put(spawnGroup, new ArrayList<>(entries.unwrap()));
                } else {
                    spawners.put(spawnGroup, new ArrayList<>());
                }
            }
        }

        private void unfreezeSpawnCost() {
            spawnSettings.mobSpawnCosts = new HashMap<>(spawnSettings.mobSpawnCosts);
        }

        public void freeze() {
            freezeSpawners();
            freezeSpawnCosts();
        }

        private void freezeSpawners() {
            Map<MobCategory, WeightedRandomList<MobSpawnSettings.SpawnerData>> spawners = new HashMap<>(spawnSettings.spawners);

            for (Map.Entry<MobCategory, List<MobSpawnSettings.SpawnerData>> entry : this.spawners.entrySet()) {
                if (entry.getValue().isEmpty()) {
                    spawners.put(entry.getKey(), WeightedRandomList.create());
                } else {
                    spawners.put(entry.getKey(), WeightedRandomList.create(entry.getValue()));
                }
            }

            spawnSettings.spawners = ImmutableMap.copyOf(spawners);
        }

        private void freezeSpawnCosts() {
            spawnSettings.mobSpawnCosts = ImmutableMap.copyOf(spawnSettings.mobSpawnCosts);
        }

        @Override
        public void setCreatureSpawnProbability(float probability) {
            spawnSettings.creatureGenerationProbability = probability;
        }

        @Override
        public void addSpawn(MobCategory spawnGroup, MobSpawnSettings.SpawnerData spawnEntry) {
            Objects.requireNonNull(spawnGroup);
            Objects.requireNonNull(spawnEntry);

            spawners.get(spawnGroup).add(spawnEntry);
        }

        @Override
        public boolean removeSpawns(BiPredicate<MobCategory, MobSpawnSettings.SpawnerData> predicate) {
            boolean anyRemoved = false;

            for (MobCategory group : MobCategory.values()) {
                if (spawners.get(group).removeIf(entry -> predicate.test(group, entry))) {
                    anyRemoved = true;
                }
            }

            return anyRemoved;
        }

        @Override
        public void setSpawnCost(EntityType<?> entityType, double mass, double gravityLimit) {
            Objects.requireNonNull(entityType);
            spawnSettings.mobSpawnCosts.put(entityType, new MobSpawnSettings.MobSpawnCost(gravityLimit, mass));
        }

        @Override
        public void clearSpawnCost(EntityType<?> entityType) {
            spawnSettings.mobSpawnCosts.remove(entityType);
        }
    }
}
