package dev.bookkeepersmc.notebook.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;

import java.util.concurrent.CompletableFuture;

public class WorldProvider extends RegistriesDatapackGenerator {
    public WorldProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries)
    {
        super(output, registries);
    }
}
