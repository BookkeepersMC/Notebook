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
package com.bookkeepersmc.notebook.registry.builder.impl;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.resources.ResourceKey;

import com.bookkeepersmc.notebook.registry.builder.RegistryAttributes;
import com.bookkeepersmc.notebook.registry.builder.RegistryHolder;

public final class RegistryHolderImpl implements RegistryHolder {
	private static final Map<ResourceKey<?>, RegistryHolder> HOLDER_MAP = new HashMap<>();

	public static RegistryHolder getHolder(ResourceKey<?> registryKey) {
		return HOLDER_MAP.computeIfAbsent(registryKey, key -> new RegistryHolderImpl());
	}

	private final EnumSet<RegistryAttributes> attributes = EnumSet.noneOf(RegistryAttributes.class);

	private RegistryHolderImpl() {
	}

	@Override
	public RegistryHolder addAttribute(RegistryAttributes attribute) {
		attributes.add(attribute);
		return this;
	}

	@Override
	public boolean hasAttribute(RegistryAttributes attribute) {
		return attributes.contains(attribute);
	}
}
