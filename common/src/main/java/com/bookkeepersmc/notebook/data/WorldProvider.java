package com.bookkeepersmc.notebook.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.data.registries.RegistryPatchGenerator;

import java.util.concurrent.CompletableFuture;

public class WorldProvider extends RegistriesDatapackGenerator {
    private final CompletableFuture<HolderLookup.Provider> registries;

    public WorldProvider(PackOutput output, CompletableFuture<RegistrySetBuilder.PatchedRegistries> registry) {
        super(output, registry.thenApply(RegistrySetBuilder.PatchedRegistries::patches));
        this.registries = registry.thenApply(RegistrySetBuilder.PatchedRegistries::full);
    }

    public WorldProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registry, RegistrySetBuilder entries) {
        this(output, RegistryPatchGenerator.createLookup(registry, entries));
    }

    public CompletableFuture<HolderLookup.Provider> getProvider() {
        return registries;
    }
}
