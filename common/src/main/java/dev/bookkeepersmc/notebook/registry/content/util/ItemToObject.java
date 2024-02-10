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

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;

public interface ItemToObject<T> {
    T get(ItemLike item);

    void add(ItemLike item, T value);

    void add(TagKey<Item> tag, T value);

    void remove(ItemLike item);

    void remove(TagKey<Item> tag);

    void clear(ItemLike item);

    void clear(TagKey<Item> tag);
}