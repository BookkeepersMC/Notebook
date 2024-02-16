package com.bookkeepersmc.notebook.registry.content.util;

import com.bookkeepersmc.notebook.event.Event;
import net.minecraft.core.RegistryAccess;

public class CommonEvents {
    private CommonEvents() {
    }
    public static final Event<TagsLoaded> TAGS_LOADED = Event.Factory.createArrayBacked(TagsLoaded.class, callbacks -> (registries, client) -> {
        for (TagsLoaded callback : callbacks) {
            callback.onTagsLoaded(registries, client);
        }
    });

    public interface TagsLoaded {
        void onTagsLoaded(RegistryAccess registries, boolean client);
    }
}
