package com.bookkeepersmc.notebook.common.world.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.bookkeepersmc.notebook.common.world.BiomeContext;
import com.bookkeepersmc.notebook.common.world.ModificationOrder;
import com.bookkeepersmc.notebook.common.world.ModifiedBiomeMarker;
import com.bookkeepersmc.notebook.common.world.SelectBiomeContext;
import com.google.common.base.Stopwatch;
import org.jetbrains.annotations.TestOnly;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

public class BiomeImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(BiomeImpl.class);

    private static final Comparator<ModifierRecord> MODIFIER_ORDER_COMPARATOR = Comparator.<ModifierRecord>comparingInt(r -> r.phase.ordinal()).thenComparingInt(r -> r.order).thenComparing(r -> r.id);

    public static final BiomeImpl INSTANCE = new BiomeImpl();

    private final List<ModifierRecord> modifiers = new ArrayList<>();

    private boolean modifiersUnsorted = true;

    private BiomeImpl() {
    }

    public void addModifier(ResourceLocation id, ModificationOrder phase, Predicate<SelectBiomeContext> selector, BiConsumer<SelectBiomeContext, BiomeContext> modifier) {
        Objects.requireNonNull(selector);
        Objects.requireNonNull(modifier);

        modifiers.add(new ModifierRecord(phase, id, selector, modifier));
        modifiersUnsorted = true;
    }

    public void addModifier(ResourceLocation id, ModificationOrder phase, Predicate<SelectBiomeContext> selector, Consumer<BiomeContext> modifier) {
        Objects.requireNonNull(selector);
        Objects.requireNonNull(modifier);

        modifiers.add(new ModifierRecord(phase, id, selector, modifier));
        modifiersUnsorted = true;
    }

    void changeOrder(ResourceLocation id, int order) {
        modifiersUnsorted = true;

        for (ModifierRecord modifierRecord : modifiers) {
            if (id.equals(modifierRecord.id)) {
                modifierRecord.setOrder(order);
            }
        }
    }

    @TestOnly
    void clearModifiers() {
        modifiers.clear();
        modifiersUnsorted = true;
    }

    private List<ModifierRecord> getSortedModifiers() {
        if (modifiersUnsorted) {
            // Resort modifiers
            modifiers.sort(MODIFIER_ORDER_COMPARATOR);
            modifiersUnsorted = false;
        }

        return modifiers;
    }

    public void finalizeWorldGen(RegistryAccess impl) {
        Stopwatch sw = Stopwatch.createStarted();

        ModifiedBiomeMarker modificationTracker = (ModifiedBiomeMarker) impl;
        modificationTracker.markModified();

        Registry<Biome> biomes = impl.registryOrThrow(Registries.BIOME);

        List<ResourceKey<Biome>> keys = biomes.entrySet().stream()
                .map(Map.Entry::getKey)
                .sorted(Comparator.comparingInt(key -> biomes.getId(biomes.getOrThrow(key))))
                .toList();

        List<ModifierRecord> sortedModifiers = getSortedModifiers();

        int biomesChanged = 0;
        int biomesProcessed = 0;
        int modifiersApplied = 0;

        for (ResourceKey<Biome> key : keys) {
            Biome biome = biomes.getOrThrow(key);

            biomesProcessed++;

            SelectBiomeContext context = new SelectBiomeContextImpl(impl, key, biome);
            BiomeContextImpl modificationContext = null;

            for (ModifierRecord modifier : sortedModifiers) {
                if (modifier.selector.test(context)) {
                    LOGGER.trace("Applying modifier {} to {}", modifier, key.location());

                    if (modificationContext == null) {
                        biomesChanged++;
                        modificationContext = new BiomeContextImpl(impl, biome);
                    }

                    modifier.apply(context, modificationContext);
                    modifiersApplied++;
                }
            }

            // Re-freeze and apply certain cleanup actions
            if (modificationContext != null) {
                modificationContext.freeze();
            }
        }

        if (biomesProcessed > 0) {
            LOGGER.info("Applied {} biome modifications to {} of {} new biomes in {}", modifiersApplied, biomesChanged,
                    biomesProcessed, sw);
        }
    }

    private static class ModifierRecord {
        private final ModificationOrder phase;

        private final ResourceLocation id;

        private final Predicate<SelectBiomeContext> selector;

        private final BiConsumer<SelectBiomeContext, BiomeContext> contextSensitiveModifier;

        private final Consumer<BiomeContext> modifier;

        // Whenever this is modified, the modifiers need to be resorted
        private int order;

        ModifierRecord(ModificationOrder phase, ResourceLocation id, Predicate<SelectBiomeContext> selector, Consumer<BiomeContext> modifier) {
            this.phase = phase;
            this.id = id;
            this.selector = selector;
            this.modifier = modifier;
            this.contextSensitiveModifier = null;
        }

        ModifierRecord(ModificationOrder phase, ResourceLocation id, Predicate<SelectBiomeContext> selector, BiConsumer<SelectBiomeContext, BiomeContext> modifier) {
            this.phase = phase;
            this.id = id;
            this.selector = selector;
            this.contextSensitiveModifier = modifier;
            this.modifier = null;
        }

        @Override
        public String toString() {
            if (modifier != null) {
                return modifier.toString();
            } else {
                return contextSensitiveModifier.toString();
            }
        }

        public void apply(SelectBiomeContext context, BiomeContextImpl modificationContext) {
            if (contextSensitiveModifier != null) {
                contextSensitiveModifier.accept(context, modificationContext);
            } else {
                modifier.accept(modificationContext);
            }
        }

        public void setOrder(int order) {
            this.order = order;
        }
    }
}
