package com.bookkeepersmc.notebook.common.world;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.bookkeepersmc.notebook.common.world.impl.BiomeImpl;
import org.jetbrains.annotations.ApiStatus;
import net.minecraft.resources.ResourceLocation;

public class BiomeModification {
    private final ResourceLocation id;

    @ApiStatus.Internal
    BiomeModification(ResourceLocation id) {
        this.id = id;
    }

    public BiomeModification add(ModificationOrder phase, Predicate<SelectBiomeContext> selector, Consumer<BiomeContext> modifier) {
        BiomeImpl.INSTANCE.addModifier(id, phase, selector, modifier);
        return this;
    }

    public BiomeModification add(ModificationOrder phase, Predicate<SelectBiomeContext> selector, BiConsumer<SelectBiomeContext, BiomeContext> modifier) {
        BiomeImpl.INSTANCE.addModifier(id, phase, selector, modifier);
        return this;
    }
}
