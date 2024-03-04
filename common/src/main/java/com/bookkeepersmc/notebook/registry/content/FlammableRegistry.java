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

import com.bookkeepersmc.notebook.registry.content.impl.FlammableRegistryImpl;
import com.bookkeepersmc.notebook.registry.content.util.BlockToObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public interface FlammableRegistry extends BlockToObject<FlammableRegistry.Entry> {
    static FlammableRegistry getDefaultInstance() {
        return getInstance(Blocks.FIRE);
    }

    static FlammableRegistry getInstance(Block block) {
        return FlammableRegistryImpl.getInstance(block);
    }

    default void add(Block block, int burn, int spread) {
        this.add(block, new Entry(burn, spread));
    }

    default void add(TagKey<Block> tag, int burn, int spread) {
        this.add(tag, new Entry(burn, spread));
    }


    final class Entry {
        private final int burn, spread;

        public Entry(int burn, int spread) {
            this.burn = burn;
            this.spread = spread;
        }

        public int getBurnChance() {
            return burn;
        }

        public int getSpreadChance() {
            return spread;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            } else {
                Entry other = (Entry) o;
                return other.burn == burn && other.spread == spread;
            }
        }

        @Override
        public int hashCode() {
            return burn * 11 + spread;
        }
    }
}
