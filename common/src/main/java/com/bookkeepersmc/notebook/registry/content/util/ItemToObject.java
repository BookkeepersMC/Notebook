package com.bookkeepersmc.notebook.registry.content.util;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface ItemToObject<T> {
    T get(ItemLike item);

    void add(ItemLike item, T value);

    void add(TagKey<Item> tag, T value);

    void remove(ItemLike item);

    void remove(TagKey<Item> tag);

    void clear(ItemLike item);

    void clear(TagKey<Item> tag);
}
