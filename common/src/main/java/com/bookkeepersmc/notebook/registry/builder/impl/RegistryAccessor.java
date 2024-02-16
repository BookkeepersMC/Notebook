package com.bookkeepersmc.notebook.registry.builder.impl;

import net.minecraft.core.WritableRegistry;

public interface RegistryAccessor<T> {
    static WritableRegistry<WritableRegistry<?>> getROOT() {
        throw new UnsupportedOperationException();
    }
}
