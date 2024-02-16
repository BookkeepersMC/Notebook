package com.bookkeepersmc.notebook.registry.content;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.vibrations.VibrationSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
* Registers Sculk frequencies
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
