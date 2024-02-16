package com.bookkeepersmc.notebook.registry.content;

import com.bookkeepersmc.notebook.registry.content.impl.CompostingRegistryImpl;
import com.bookkeepersmc.notebook.registry.content.util.ItemToObject;

public interface CompostingRegistry extends ItemToObject<Float> {
    CompostingRegistry INSTANCE = new CompostingRegistryImpl();
}
