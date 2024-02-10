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

import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;


@ApiStatus.NonExtendable
public abstract class Event<T> {
    protected volatile T invoker;

    public final T invoker() {
        return invoker;
    }
    public abstract void register(T listener);
    public static final ResourceLocation DEFAULT_PHASE = new ResourceLocation("fabric", "default");

    public void register(ResourceLocation phase, T listener) {
        register(listener);
    }

    public void addPhaseOrdering(ResourceLocation firstPhase, ResourceLocation secondPhase) {
    }
    public static final class Factory {

        public static <T> Event<T> createArrayBacked(Class<? super T> type, Function<T[], T> factory) {
            return EventImpl.create(type, factory);
        }
    }
}
