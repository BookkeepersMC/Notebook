package com.bookkeepersmc.notebook.registry.content.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public interface BlockToObject<T> {
    T get(Block block);

    void add(Block block, T value);
    void add(TagKey<Block> tag, T value);
    void remove(Block block);
    void remove(TagKey<Block> tag);
    void clear(Block block);
    void clear(TagKey<Block> tag);
}
