/*
 * Copyright (c) 2023, 2024 BookkeepersMC under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.bookkeepersmc.notebook.registry.content;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
* Registers Sculk Sensor Frequency
*/
public final class SculkSensorRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(SculkSensorRegistry.class);

    private SculkSensorRegistry() {
    }

    // Register your frequency
    public static void register(GameEvent event, int frequency) {
        if (frequency <= 0 || frequency >= 16) {
            throw new IllegalArgumentException("Attempted to register Sculk Sensor frequency for event " + BuiltInRegistries.GAME_EVENT.getKey(event) + "with frequency " + frequency + ". Sculk Sensor frequencies must be between 1 and 15.");
        }
        final Object2IntOpenHashMap<GameEvent> map = (Object2IntOpenHashMap<GameEvent>) VibrationSystem.VIBRATION_FREQUENCY_FOR_EVENT;
        int replaced = map.put(event, frequency);
        if (replaced != 0) {
            LOGGER.debug("Replaced old frequency mapping for {} - was {}, now {}", BuiltInRegistries.GAME_EVENT.getKey(event), replaced, frequency);
        }
    }
}
