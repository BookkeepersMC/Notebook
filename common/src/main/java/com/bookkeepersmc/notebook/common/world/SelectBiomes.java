package com.bookkeepersmc.notebook.common.world;

import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;

import com.bookkeepersmc.notebook.common.world.impl.BuiltInKeys;
import com.google.common.collect.ImmutableSet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.dimension.LevelStem;

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
