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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import com.bookkeepersmc.notebook.mixin.ShovelAccessor;

public final class FlattenableRegistry {
	private static final Logger LOGGER = LoggerFactory.getLogger(FlattenableRegistry.class);
	private FlattenableRegistry() {
	}

	public static void registerFlattenable(Block input, BlockState flat) {
		Objects.requireNonNull(input, "Input block cannot be null");
		Objects.requireNonNull(flat, "Flattened block state cannot be null");
		BlockState old = ShovelAccessor.getPathState().put(input, flat);

		if (old != null) {
			LOGGER.debug("Replaced old flattening mapping from {} to {} with {}", input, old, flat);
		}
	}
}
