package dev.bookkeepersmc.notebook.registry.content;

import dev.bookkeepersmc.notebook.registry.content.impl.CompostingRegistryImpl;
import dev.bookkeepersmc.notebook.registry.content.util.ItemToObject;

public interface CompostingRegistry extends ItemToObject<Float> {
    CompostingRegistry INSTANCE = new CompostingRegistryImpl();
}
