package dev.bookkeepersmc.notebook.mixin;

import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
import java.util.Set;

@Mixin(Villager.class)
public interface VillagerAccessor {
    @Mutable
    @Accessor("FOOD_POINTS")
    static void setFoodValues(Map<Item, Integer> items) {
        throw new AssertionError("Untransformed Accessor!");
    }

    @Mutable
    @Accessor("WANTED_ITEMS")
    static void setGatherable(Set<Item> items) {
        throw new AssertionError("Untransformed Accessor!");
    }

    @Mutable
    @Accessor("WANTED_ITEMS")
    static Set<Item> getGatherable() {
        throw new AssertionError("Untransformed Accessor!");
    }
}
