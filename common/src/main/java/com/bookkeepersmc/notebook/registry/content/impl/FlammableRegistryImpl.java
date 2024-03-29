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

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import com.bookkeepersmc.notebook.registry.content.FlammableRegistry;
import com.bookkeepersmc.notebook.registry.content.util.CommonEvents;


public class FlammableRegistryImpl implements FlammableRegistry {
	private static final FlammableRegistry.Entry REMOVED = new FlammableRegistry.Entry(0, 0);
	private static final Map<Block, FlammableRegistryImpl> REGISTRIES = new HashMap<>();

	private final Map<Block, FlammableRegistry.Entry> registeredEntriesBlock = new HashMap<>();
	private final Map<TagKey<Block>, FlammableRegistry.Entry> registeredEntriesTag = new HashMap<>();
	private volatile Map<Block, FlammableRegistry.Entry> computedEntries = null;
	private final Block key;

	private FlammableRegistryImpl(Block key) {
		this.key = key;

		// Reset computed values after tags change since they depend on tags.
		CommonEvents.TAGS_LOADED.register((registries, client) -> {
			computedEntries = null;
		});
	}

	private Map<Block, FlammableRegistry.Entry> getEntryMap() {
		Map<Block, FlammableRegistry.Entry> ret = computedEntries;

		if (ret == null) {
			ret = new IdentityHashMap<>();

			// tags take precedence over blocks
			for (TagKey<Block> tag : registeredEntriesTag.keySet()) {
				FlammableRegistry.Entry entry = registeredEntriesTag.get(tag);

				for (Holder<Block> block : BuiltInRegistries.BLOCK.getTagOrEmpty(tag)) {
					ret.put(block.value(), entry);
				}
			}

			ret.putAll(registeredEntriesBlock);

			computedEntries = ret;
		}

		return ret;
	}

	// User-facing fire registry interface - queries vanilla fire block
	@Override
	public Entry get(Block block) {
		Entry entry = getEntryMap().get(block);

		if (entry != null) {
			return entry;
		} else {
			return ((FireHooks) key).getEntry(block.defaultBlockState());
		}
	}

	public Entry getFabric(Block block) {
		return getEntryMap().get(block);
	}

	@Override
	public void add(Block block, Entry value) {
		registeredEntriesBlock.put(block, value);

		computedEntries = null;
	}

	@Override
	public void add(TagKey<Block> tag, Entry value) {
		registeredEntriesTag.put(tag, value);

		computedEntries = null;
	}

	@Override
	public void remove(Block block) {
		add(block, REMOVED);
	}

	@Override
	public void remove(TagKey<Block> tag) {
		add(tag, REMOVED);
	}

	@Override
	public void clear(Block block) {
		registeredEntriesBlock.remove(block);

		computedEntries = null;
	}

	@Override
	public void clear(TagKey<Block> tag) {
		registeredEntriesTag.remove(tag);

		computedEntries = null;
	}

	public static FlammableRegistryImpl getInstance(Block block) {
		if (!(block instanceof FireHooks)) {
			throw new RuntimeException("Not a hookable fire block: " + block);
		}

		return REGISTRIES.computeIfAbsent(block, FlammableRegistryImpl::new);
	}
}
