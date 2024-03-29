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
package com.bookkeepersmc.notebook.registry.content.impl;

import java.util.IdentityHashMap;
import java.util.Map;

import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;

import com.bookkeepersmc.notebook.registry.content.FuelRegistry;
import com.bookkeepersmc.notebook.registry.content.util.CommonEvents;

public class FuelRegistryImpl implements FuelRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(FuelRegistryImpl.class);
	private final Object2IntMap<ItemLike> itemCookTimes = new Object2IntLinkedOpenHashMap<>();
	private final Object2IntMap<TagKey<Item>> tagCookTimes = new Object2IntLinkedOpenHashMap<>();
	private volatile Map<Item, Integer> fuelTimeCache = null;

	public FuelRegistryImpl() {
		CommonEvents.TAGS_LOADED.register((registries, client) -> {
			reset();
		});
	}
	public Map<Item, Integer> getTime() {
		Map<Item, Integer> ret = fuelTimeCache;
		if (ret == null) {
			fuelTimeCache = ret = new IdentityHashMap<>(AbstractFurnaceBlockEntity.getFuel());
		}
		return ret;
	}

	public void reset() {fuelTimeCache = null;}

	@Override
	public Integer get(ItemLike item) {
		return getTime().get(item.asItem());
	}

	@Override
	public void add(ItemLike item, Integer cookingTime) {
		if (cookingTime > 32767) {
			LOGGER.warn("The time to cook " + item + "is bigger than 32767!");
		}
		itemCookTimes.put(item, cookingTime.intValue());
		reset();
	}

	@Override
	public void add(TagKey<Item> tag, Integer cookingTime) {
		if (cookingTime > 32767) {
			LOGGER.warn("The time to cook " + tag + "is bigger than 32767!");
		}

		tagCookTimes.put(tag, cookingTime.intValue());
		reset();
	}

	@Override
	public void remove(ItemLike item) {
		add(item, 0);
		reset();
	}

	@Override
	public void remove(TagKey<Item> tag) {
		add(tag, 0);
		reset();
	}

	@Override
	public void clear(ItemLike item) {
		itemCookTimes.removeInt(item);
		reset();
	}

	@Override
	public void clear(TagKey<Item> tag) {
		tagCookTimes.removeInt(tag);
		reset();
	}
	public void apply(Map<Item, Integer> map) {
		for (TagKey<Item> tag : tagCookTimes.keySet()) {
			int time = tagCookTimes.getInt(tag);

			if (time <= 0) {
				for (Holder<Item> key : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
					final Item item = key.value();
					map.remove(item);
				}
			} else {
				AbstractFurnaceBlockEntity.add(map, tag, time);
			}
		}
		for (ItemLike item : itemCookTimes.keySet()) {
			int time = itemCookTimes.getInt(item);

			if (time <= 0) {
				map.remove(item.asItem());
			} else {
				AbstractFurnaceBlockEntity.add(map, item, time);
			}
		}

	}
	// AbstractFurnaceBlockEntity methods, edited because Normal crashes gradlew
	private static boolean isNotFuel(Item item) {
		return item.builtInRegistryHolder().is(ItemTags.NON_FLAMMABLE_WOOD);
	}
	public static void addFuel(Map<Item, Integer> map, TagKey<Item> tag, int i) {
		for(Holder<Item> holder : BuiltInRegistries.ITEM.getTagOrEmpty(tag)) {
			if (!isNotFuel(holder.value())) {
				map.put(holder.value(), i);
			}
		}
	}

	public static void addFuel(Map<Item, Integer> map, ItemLike itemLike, int i) {
		Item item = itemLike.asItem();
		if (isNotFuel(item)) {
			if (SharedConstants.IS_RUNNING_IN_IDE) {
				throw (IllegalStateException)Util.pauseInIde(
						new IllegalStateException(
								"A developer tried to explicitly make fire resistant item " + item.getName(null).getString() + " a furnace fuel. That will not work!"
						)
				);
			}
		} else {
			map.put(item, i);
		}
	}
}
