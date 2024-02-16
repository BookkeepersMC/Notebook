package com.bookkeepersmc.notebook.data;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;


/*
* A alternative to Bootstap Context, removed in 1.20.5 snapshots
* Allows registering of Features
*/
public interface RegistryContext<T> {
    Holder.Reference<T> register(ResourceKey<T> key, T value, Lifecycle lifecycle);

    default Holder.Reference<T> register(ResourceKey<T> key, T value) {
        return this.register(key, value, Lifecycle.stable());
    }

    <S>HolderGetter<S> getRegistryLookup(ResourceKey<? extends Registry<? extends S>> registerRef);
}
