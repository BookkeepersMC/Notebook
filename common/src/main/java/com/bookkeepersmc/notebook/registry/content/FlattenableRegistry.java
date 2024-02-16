package com.bookkeepersmc.notebook.registry.content;

import com.bookkeepersmc.notebook.mixin.ShovelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public final class FlattenableRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(FlattenableRegistry.class);
    private FlattenableRegistry() {
    }

    public static void registerFlattenable(Block input, BlockState flat) {
        Objects.requireNonNull(input, "Input block cannot be null");
        Objects.requireNonNull(flat, "Flattened block state cannot be null");
        BlockState old = ShovelAccessor.getPathState().put(input, flat);

        if (old != null) {
            LOGGER.debug("Replaced old flattening mapping from {} to {} with {}", input, old, flat);
        }
    }
}
