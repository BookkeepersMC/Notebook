package dev.bookkeepersmc.notebook.registry.content;

import net.minecraft.world.item.HoneycombItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.Objects;

public final class OxidizableRegistry {
    private OxidizableRegistry() {
    }

    public static void registerOxidizable(Block less, Block more) {
        Objects.requireNonNull(less, "Oxidizable block cannot be null!");
        Objects.requireNonNull(more, "Oxidizable block cannot be null!");
        WeatheringCopper.NEXT_BY_BLOCK.get().put(less, more);
    }
    public static void registerWaxable(Block unwaxed, Block waxed) {
        Objects.requireNonNull(unwaxed, "Unwaxed block cannot be null!");
        Objects.requireNonNull(waxed, "Waxed block cannot be null!");
        HoneycombItem.WAXABLES.get().put(unwaxed, waxed);
    }
}
