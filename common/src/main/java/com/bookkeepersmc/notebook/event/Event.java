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
package com.bookkeepersmc.notebook.event;

import java.util.function.Function;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.resources.ResourceLocation;


@ApiStatus.NonExtendable
public abstract class Event<T> {
	protected volatile T invoker;

	public final T invoker() {
		return invoker;
	}
	public abstract void register(T listener);
	public static final ResourceLocation DEFAULT_PHASE = new ResourceLocation("fabric", "default");

	public void register(ResourceLocation phase, T listener) {
		register(listener);
	}

	public void addPhaseOrdering(ResourceLocation firstPhase, ResourceLocation secondPhase) {
	}
	public static final class Factory {

		public static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> factory) {
			return EventImpl.create(type, factory);
		}
	}
}
