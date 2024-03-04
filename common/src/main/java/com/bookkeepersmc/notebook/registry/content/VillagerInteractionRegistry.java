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

package com.bookkeepersmc.notebook.registry.content;

import com.bookkeepersmc.notebook.mixin.FarmerAccessor;
import com.bookkeepersmc.notebook.mixin.GiveGiftsToHeroAccessor;
import com.bookkeepersmc.notebook.mixin.VillagerAccessor;
import com.bookkeepersmc.notebook.registry.content.util.ImmutableUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class VillagerInteractionRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(VillagerInteractionRegistry.class);

    private VillagerInteractionRegistry() {
    }

    public static void registerCollectable(ItemLike item) {
        Objects.requireNonNull(item.asItem(), "Item cannot be null!");
        getCollectableRegistry().add(item.asItem());
    }

    public static void registerCompostable(ItemLike item) {
        Objects.requireNonNull(item.asItem(), "Item cannot be null!");
        getCompostableRegistry().add(item.asItem());
    }

    public static void registerFood(ItemLike item, int foodValue) {
        Objects.requireNonNull(item.asItem(), "Item cannot be null!");
        Integer oldValue = getFoodRegistry().put(item.asItem(), foodValue);

        if (oldValue != null) {
            LOGGER.info("Overriding previous food value of {}, was: {}, now: {}", item.asItem().toString(), oldValue, foodValue);
        }
    }

    public static void registerGiftLootTable(VillagerProfession profession, ResourceLocation lootTable) {
        Objects.requireNonNull(profession, "Profession cannot be null!");
        Objects.requireNonNull(lootTable, "Loot table identifier cannot be null!");
        ResourceLocation oldValue = GiveGiftsToHeroAccessor.getGifts().put(profession, lootTable);
        if (oldValue != null) {
            LOGGER.info("Overriding previous gift loot table of {} profession, was: {}, now: {}", profession.name(), oldValue, lootTable);
        }
    }

    private static Set<Item> getCollectableRegistry() {
        return ImmutableUtils.getAsSet(VillagerAccessor::getGatherable, VillagerAccessor::setGatherable);
    }

    private static List<Item> getCompostableRegistry() {
        return ImmutableUtils.getAsList(FarmerAccessor::getCompostables, FarmerAccessor::setCompostables);
    }

    private static Map<Item, Integer> getFoodRegistry() {
        return ImmutableUtils.getAsMap(() -> Villager.FOOD_POINTS, VillagerAccessor::setFoodValues);
    }
}
