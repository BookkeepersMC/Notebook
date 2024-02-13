package dev.bookkeepersmc.notebook.registry.content;

import dev.bookkeepersmc.notebook.mixin.FarmerAccessor;
import dev.bookkeepersmc.notebook.mixin.GiveGiftsToHeroAccessor;
import dev.bookkeepersmc.notebook.mixin.VillagerAccessor;
import dev.bookkeepersmc.notebook.registry.content.util.ImmutableUtils;
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
