package com.bookkeepersmc.notebook.registry.content;

import com.bookkeepersmc.notebook.registry.content.impl.FuelRegistryImpl;
import com.bookkeepersmc.notebook.registry.content.util.ItemToObject;

public interface FuelRegistry extends ItemToObject<Integer> {
    FuelRegistry FUEL = new FuelRegistryImpl();
}
