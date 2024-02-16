package com.bookkeepersmc.notebook.common.world;

import com.google.common.base.Preconditions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.Predicate;

public final class BiomeRegistry {
    private BiomeRegistry() {
    }

    public static void addFeature(Predicate<SelectBiomeContext> biomeSelector, GenerationStep.Decoration step, ResourceKey<PlacedFeature> placedFeatureRegistryKey) {
        create(placedFeatureRegistryKey.location()).add(ModificationOrder.ADDITIONS, biomeSelector, context -> {
            context.getGenerationSettings().addFeature(step, placedFeatureRegistryKey);
        });
    }

    public static void addCarver(Predicate<SelectBiomeContext> biomeSelector, GenerationStep.Carving step, ResourceKey<ConfiguredWorldCarver<?>> configuredCarverKey) {
        create(configuredCarverKey.location()).add(ModificationOrder.ADDITIONS, biomeSelector, context -> {
            context.getGenerationSettings().addCarver(step, configuredCarverKey);
        });
    }

    public static void addSpawn(Predicate<SelectBiomeContext> biomeSelector,
                                MobCategory spawnGroup, EntityType<?> entityType,
                                int weight, int minGroupSize, int maxGroupSize) {
        Preconditions.checkArgument(entityType.getCategory() != MobCategory.MISC,
                "Cannot add spawns for entities with  MISC spawnGroup since they'd be replaced by pigs.");

        ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entityType);
        Preconditions.checkState(BuiltInRegistries.ENTITY_TYPE.getResourceKey(entityType).isPresent(), "Unregistered entity type: %s", entityType);

        create(id).add(ModificationOrder.ADDITIONS, biomeSelector, context -> {
            context.getSpawnSettings().addSpawn(spawnGroup, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        });
    }

    public static BiomeModification create(ResourceLocation id) {
        return new BiomeModification(id);
    }
}
