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
package com.bookkeepersmc.notebook.registry.builder;

import java.util.EnumSet;

import com.mojang.serialization.Lifecycle;

import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import com.bookkeepersmc.notebook.registry.builder.impl.RegistryAccessor;

public class RegistryBuilder<T, R extends WritableRegistry<T>> {

	public static <T, R extends WritableRegistry<T>> RegistryBuilder<T, R> from(R registry) {
		return new RegistryBuilder<>(registry);
	}

	public static <T> RegistryBuilder<T, MappedRegistry<T>> createSimple(ResourceKey<Registry<T>> resourceKey) {
		return from(new MappedRegistry<>(resourceKey, Lifecycle.stable(), false));
	}

	public static <T> RegistryBuilder<T, MappedRegistry<T>> createSimple(Class<T> type, ResourceLocation id) {
		return createSimple(ResourceKey.createRegistryKey(id));
	}

	public static <T> RegistryBuilder<T, DefaultedMappedRegistry<T>> createDefaulted(ResourceKey<Registry<T>> resourceKey, ResourceLocation id) {
		return from(new DefaultedMappedRegistry<T>(id.toString(), resourceKey, Lifecycle.stable(), false));
	}

	private final R resource;
	private final EnumSet<RegistryAttributes> attributes = EnumSet.noneOf(RegistryAttributes.class);

	private RegistryBuilder(R resource) {
		this.resource = resource;
		attribute(RegistryAttributes.IS_MODDED);
	}

	public RegistryBuilder<T, R> attribute(RegistryAttributes attributes) {
		this.attributes.add(attributes);
		return this;
	}

	public R buildAndRegister() {
		final ResourceKey<?> key = resource.key();
		for (RegistryAttributes attributes : attributes) {
			RegistryHolder.get(key).addAttribute(attributes);
		}
		RegistryAccessor.getROOT().register((ResourceKey<WritableRegistry<?>>) key, resource, Lifecycle.stable());
		return resource;
	}
}
