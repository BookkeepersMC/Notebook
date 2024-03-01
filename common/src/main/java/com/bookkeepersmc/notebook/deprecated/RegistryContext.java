package com.bookkeepersmc.notebook.deprecated;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;


/**
 * An alternative to Bootstap Context, removed in 1.20.5 snapshots
 * Well I'm dumb. It's a TYPO! Added in as Bootstrap Context haha
 * Allows registering of Features
 */
@Deprecated(forRemoval = true)
public interface RegistryContext<T> {
    Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle);

    default Holder.Reference<T> register(ResourceKey<T> key, T value) {
        return this.register(key, value, Lifecycle.stable());
    }

    <S>HolderGetter<S> getRegistryLookup(ResourceKey<? extends Registry<? extends S>> registerRef);
}
