package com.bookkeepersmc.notebook.registry.builder;

import com.mojang.serialization.Lifecycle;
import com.bookkeepersmc.notebook.registry.builder.impl.RegistryAccessor;
import net.minecraft.core.DefaultedMappedRegistry;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.EnumSet;

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
