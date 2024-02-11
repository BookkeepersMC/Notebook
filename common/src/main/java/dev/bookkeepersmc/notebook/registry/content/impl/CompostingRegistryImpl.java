package dev.bookkeepersmc.notebook.registry.content.impl;

import dev.bookkeepersmc.notebook.registry.content.CompostingRegistry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ComposterBlock;

public class CompostingRegistryImpl implements CompostingRegistry {
    @Override
    public Float get(ItemLike item) {
        return ComposterBlock.COMPOSTABLES.getOrDefault(item.asItem(), 0.0F);
    }

    @Override
    public void add(ItemLike item, Float value) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), value);
    }

    @Override
    public void add(TagKey<Item> tag, Float value) {
        throw new UnsupportedOperationException("Tags are not currently supported!");
    }

    @Override
    public void remove(ItemLike item) {
        ComposterBlock.COMPOSTABLES.removeFloat(item.asItem());
    }

    @Override
    public void remove(TagKey<Item> tag) {
        throw new UnsupportedOperationException("Tags are not currently supported!");
    }

    @Override
    public void clear(ItemLike item) {
        throw new UnsupportedOperationException("CompostingRegistry operates directly on the vanilla map - clearing not supported!");
    }

    @Override
    public void clear(TagKey<Item> tag) {
        throw new UnsupportedOperationException("CompostingRegistry operates directly on the vanilla map - clearing not supported!");
    }
}
