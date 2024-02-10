package dev.bookkeepersmc.notebook.registry.content.impl;

import dev.bookkeepersmc.notebook.registry.content.FlammableRegistry;
import net.minecraft.world.level.block.state.BlockState;

public interface FireHooks {
    FlammableRegistry.Entry getEntry(BlockState block);
}
