package com.bookkeepersmc.notebook.registry.builder.impl;

import com.bookkeepersmc.notebook.registry.builder.RegistryAttributes;
import com.bookkeepersmc.notebook.registry.builder.RegistryHolder;
import net.minecraft.resources.ResourceKey;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

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
