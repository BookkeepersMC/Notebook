package com.bookkeepersmc.notebook.registry.content;

import com.bookkeepersmc.notebook.mixin.AxeAccessor;
import com.bookkeepersmc.notebook.registry.content.util.ImmutableUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.Map;
import java.util.Objects;

public final class StrippableRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(StrippableRegistry.class);
    private StrippableRegistry() {
    }

    public static void registerStripped(Block input, Block stripped) {
        requireNonNullAndAxis(input, "input block");
        requireNonNullAndAxis(stripped, "stripped block");
        Block old = getRegistry().put(input, stripped);

        if (old != null) {
            LOGGER.debug("Replaced old stripping mapping from {} to {} with {}", input, old, stripped);
        }

    }

    private static void requireNonNullAndAxis(Block block, String name) {
        Objects.requireNonNull(block, name + " cannot be null");
        if (!block.getStateDefinition().getProperties().contains(BlockStateProperties.AXIS)) {
            throw new IllegalArgumentException(name + " must have the axis property!");
        }
    }
    private static Map<Block, Block> getRegistry() {
        return ImmutableUtils.getAsMap(AxeAccessor::getStripped, AxeAccessor::setStripped);
    }
}
