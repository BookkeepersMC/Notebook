package com.bookkeepersmc.notebook.screen;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.Objects;
/**
 * Use when registering a {@link MenuType}
 */
public class ScreenHandlerType<T extends AbstractContainerMenu> extends MenuType<T> {
    private final ScreenFactory<T> factory;

    public ScreenHandlerType(ScreenFactory<T> factory) {
        super(null, FeatureFlags.VANILLA_SET);
        this.factory = Objects.requireNonNull(factory, "screen handler factory cannot be null");
    }

    @Override
    public final T create(int id, Inventory inv) {
        throw new UnsupportedOperationException("Use ExtendedScreenHandlerType.create(int, PlayerInventory, PacketByteBuf)!");
    }

    public T create(int id, Inventory inv, FriendlyByteBuf buf) {
        return factory.create(id, inv, buf);
    }

    @FunctionalInterface
    public interface ScreenFactory<T extends AbstractContainerMenu> {
        T create(int id, Inventory inv, FriendlyByteBuf buf);
    }
}
