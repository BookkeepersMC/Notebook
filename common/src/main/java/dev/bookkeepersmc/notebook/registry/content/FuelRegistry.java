package dev.bookkeepersmc.notebook.registry.content;

import dev.bookkeepersmc.notebook.registry.content.impl.FuelRegistryImpl;
import dev.bookkeepersmc.notebook.registry.content.util.ItemToObject;

public interface FuelRegistry extends ItemToObject<Integer> {
    FuelRegistry FUEL = new FuelRegistryImpl();
}
