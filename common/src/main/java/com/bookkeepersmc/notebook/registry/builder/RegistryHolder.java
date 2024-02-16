package com.bookkeepersmc.notebook.registry.builder;

import com.bookkeepersmc.notebook.registry.builder.impl.RegistryHolderImpl;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryHolder {
    static RegistryHolder get(ResourceKey<?> resourceKey) {
        return RegistryHolderImpl.getHolder(resourceKey);
    }
    static RegistryHolder get(Registry<?> registry) {
        return get(registry.key());
    }

    RegistryHolder addAttribute(RegistryAttributes attribute);

    boolean hasAttribute(RegistryAttributes attribute);
}
