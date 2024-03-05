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

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

import com.mojang.datafixers.util.Pair;

import net.minecraft.world.item.HoeItem;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.bookkeepersmc.notebook.mixin.HoeAccessor;

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
