package com.bookkeepersmc.notebook.registry.content;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;
import org.apache.commons.compress.utils.Lists;

import java.util.List;

public class PotionMaker {
    public static final List<PotionMaker.Mixer<Potion>> POTION_MIX = Lists.newArrayList();
    public static final List<PotionMaker.Mixer<Item>> CONTAINER_MIX = Lists.newArrayList();

    public static class Mixer<T> {
        final T from;
        final Ingredient ingredient;
        final T to;

        protected Mixer(T from, Ingredient ingredient, T to) {
            this.from = from;
            this.ingredient = ingredient;
            this.to = to;
        }
    }
}
