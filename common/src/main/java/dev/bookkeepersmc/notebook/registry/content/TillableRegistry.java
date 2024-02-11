package dev.bookkeepersmc.notebook.registry.content;

import dev.bookkeepersmc.notebook.mixin.HoeAccessor;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class TillableRegistry {
    private TillableRegistry() {
    }

    public static void registerTillable(Block input, Predicate<UseOnContext> usage, Consumer<UseOnContext> tillingAction) {
        Objects.requireNonNull(input, "Input block cannot be null!");
        HoeAccessor.getTillingActions().put(input, Pair.of(usage, tillingAction));
    }
    public static void registerTillable(Block input, Predicate<UseOnContext> usagePredicate, BlockState tilled) {
        Objects.requireNonNull(tilled, "Tilled block state cannot be null");
        registerTillable(input, usagePredicate, HoeItem.changeIntoState(tilled));
    }
    public static void registerTillable(Block input, Predicate<UseOnContext> usagePredicate, BlockState tilled, ItemLike droppedItem) {
        Objects.requireNonNull(tilled, "Tilled block state cannot be null");
        Objects.requireNonNull(droppedItem, "Dropped item cannot be null");
        TillableRegistry.registerTillable(input, usagePredicate, HoeItem.changeIntoStateAndDropItem(tilled, droppedItem));
    }
}
