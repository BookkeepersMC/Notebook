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

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Collectors;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import org.jetbrains.annotations.Nullable;

public final class TheEndData {
    public static final ThreadLocal<HolderGetter<Biome>> biomeRegistry = new ThreadLocal<>();
    public static final Set<ResourceKey<Biome>> ADDED_BIOMES = new HashSet<>();
    private static final Map<ResourceKey<Biome>, Picker<ResourceKey<Biome>>> END_BIOMES_MAP = new IdentityHashMap<>();
    private static final Map<ResourceKey<Biome>, Picker<ResourceKey<Biome>>> END_MIDLANDS_MAP = new IdentityHashMap<>();
    private static final Map<ResourceKey<Biome>, Picker<ResourceKey<Biome>>> END_BARRENS_MAP = new IdentityHashMap<>();

    static {
        END_BIOMES_MAP.computeIfAbsent(Biomes.THE_END, key -> new Picker<>())
                .add(Biomes.THE_END, 1.0);
        END_BIOMES_MAP.computeIfAbsent(Biomes.END_HIGHLANDS, key -> new Picker<>())
                .add(Biomes.END_HIGHLANDS, 1.0);
        END_BIOMES_MAP.computeIfAbsent(Biomes.SMALL_END_ISLANDS, key -> new Picker<>())
                .add(Biomes.SMALL_END_ISLANDS, 1.0);

        END_MIDLANDS_MAP.computeIfAbsent(Biomes.END_HIGHLANDS, key -> new Picker<>())
                .add(Biomes.END_MIDLANDS, 1.0);
        END_BARRENS_MAP.computeIfAbsent(Biomes.END_HIGHLANDS, key -> new Picker<>())
                .add(Biomes.END_BARRENS, 1.0);
    }

    private TheEndData() {
    }

    public static void addEndBiomeReplacement(ResourceKey<Biome> replaced, ResourceKey<Biome> variant, double weight) {
        Preconditions.checkNotNull(replaced, "replaced entry is null");
        Preconditions.checkNotNull(variant, "variant entry is null");
        Preconditions.checkArgument(weight > 0.0, "Weight is less than or equal to 0.0 (got %s)", weight);
        END_BIOMES_MAP.computeIfAbsent(replaced, key -> new Picker<>()).add(variant, weight);
        ADDED_BIOMES.add(variant);
    }

    public static void addEndMidlandsReplacement(ResourceKey<Biome> highlands, ResourceKey<Biome> midlands, double weight) {
        Preconditions.checkNotNull(highlands, "highlands entry is null");
        Preconditions.checkNotNull(midlands, "midlands entry is null");
        Preconditions.checkArgument(weight > 0.0, "Weight is less than or equal to 0.0 (got %s)", weight);
        END_MIDLANDS_MAP.computeIfAbsent(highlands, key -> new Picker<>()).add(midlands, weight);
        ADDED_BIOMES.add(midlands);
    }

    public static void addEndBarrensReplacement(ResourceKey<Biome> highlands, ResourceKey<Biome> barrens, double weight) {
        Preconditions.checkNotNull(highlands, "highlands entry is null");
        Preconditions.checkNotNull(barrens, "midlands entry is null");
        Preconditions.checkArgument(weight > 0.0, "Weight is less than or equal to 0.0 (got %s)", weight);
        END_BARRENS_MAP.computeIfAbsent(highlands, key -> new Picker<>()).add(barrens, weight);
        ADDED_BIOMES.add(barrens);
    }

    public static Overrides createOverrides(HolderGetter<Biome> biomes) {
        return new Overrides(biomes);
    }

    public static class Overrides {
        public final Set<Holder<Biome>> customBiomes;

        private final Holder<Biome> endMidlands;
        private final Holder<Biome> endBarrens;
        private final Holder<Biome> endHighlands;

        private final @Nullable Map<Holder<Biome>, Picker<Holder<Biome>>> endBiomesMap;
        private final @Nullable Map<Holder<Biome>, Picker<Holder<Biome>>> endMidlandsMap;
        private final @Nullable Map<Holder<Biome>, Picker<Holder<Biome>>> endBarrensMap;

        private final Map<Climate.Sampler, ImprovedNoise> samplers = new WeakHashMap<>();

        public Overrides(HolderGetter<Biome> biomeRegistry) {
            this.customBiomes = ADDED_BIOMES.stream().map(biomeRegistry::getOrThrow).collect(Collectors.toSet());

            this.endMidlands = biomeRegistry.getOrThrow(Biomes.END_MIDLANDS);
            this.endBarrens = biomeRegistry.getOrThrow(Biomes.END_BARRENS);
            this.endHighlands = biomeRegistry.getOrThrow(Biomes.END_HIGHLANDS);

            this.endBiomesMap = resolveOverrides(biomeRegistry, END_BIOMES_MAP, Biomes.THE_END);
            this.endMidlandsMap = resolveOverrides(biomeRegistry, END_MIDLANDS_MAP, Biomes.END_MIDLANDS);
            this.endBarrensMap = resolveOverrides(biomeRegistry, END_BARRENS_MAP, Biomes.END_BARRENS);
        }

        private @Nullable Map<Holder<Biome>, Picker<Holder<Biome>>> resolveOverrides(HolderGetter<Biome> biomeRegistry, Map<ResourceKey<Biome>, Picker<ResourceKey<Biome>>> overrides, ResourceKey<Biome> vanillaKey) {
            Map<Holder<Biome>, Picker<Holder<Biome>>> result = new Object2ObjectOpenCustomHashMap<>(overrides.size(), RegistryKeyHashStrategy.INSTANCE);

            for (Map.Entry<ResourceKey<Biome>, Picker<ResourceKey<Biome>>> entry : overrides.entrySet()) {
                Picker<ResourceKey<Biome>> picker = entry.getValue();
                int count = picker.getEntryCount();
                if (count == 0 || (count == 1 && entry.getKey() == vanillaKey)) continue;
                result.put(biomeRegistry.getOrThrow(entry.getKey()), picker.map(biomeRegistry::getOrThrow));
            }

            return result.isEmpty() ? null : result;
        }

        public Holder<Biome> pick(int x, int y, int z, Climate.Sampler noise, Holder<Biome> vanillaBiome) {
            boolean isMidlands = vanillaBiome.is(endMidlands::is);

            if (isMidlands || vanillaBiome.is(endBarrens::is)) {
                Holder<Biome> highlandsReplacement = pick(endHighlands, endHighlands, endBiomesMap, x, z, noise);
                Map<Holder<Biome>, Picker<Holder<Biome>>> map = isMidlands ? endMidlandsMap : endBarrensMap;

                return pick(highlandsReplacement, vanillaBiome, map, x, z, noise);
            } else {
                assert END_BIOMES_MAP.containsKey(vanillaBiome.unwrapKey().orElseThrow());

                return pick(vanillaBiome, vanillaBiome, endBiomesMap, x, z, noise);
            }
        }

        private <T extends Holder<Biome>> T pick(T key, T defaultValue, Map<T, Picker<T>> pickers, int x, int z, Climate.Sampler noise) {
            if (pickers == null) return defaultValue;

            Picker<T> picker = pickers.get(key);
            if (picker == null) return defaultValue;
            int count = picker.getEntryCount();
            if (count == 0 || (count == 1 && key.is(endHighlands::is))) return defaultValue;
            return picker.pickFromNoise(((SamplerHooks) (Object) noise).getEndBiomesSampler(), x / 64.0, 0, z / 64.0);
        }
    }

    enum RegistryKeyHashStrategy implements Hash.Strategy<Holder<?>> {
        INSTANCE;
        @Override
        public boolean equals(Holder<?> a, Holder<?> b) {
            if (a == b) return true;
            if (a == null || b == null) return false;
            if (a.kind() != b.kind()) return false;
            return a.unwrap().map(key -> b.unwrapKey().get() == key, b.value()::equals);
        }

        @Override
        public int hashCode(Holder<?> a) {
            if (a == null) return 0;
            return a.unwrap().map(System::identityHashCode, Object::hashCode);
        }
    }
}
