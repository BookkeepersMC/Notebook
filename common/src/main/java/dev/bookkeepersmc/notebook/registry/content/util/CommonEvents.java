/*
 * Copyright (c) 2016, 2017, 2018, 2019 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package dev.bookkeepersmc.notebook.registry.content.util;

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
