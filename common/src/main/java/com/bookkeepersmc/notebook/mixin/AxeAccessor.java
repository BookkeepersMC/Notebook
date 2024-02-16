package com.bookkeepersmc.notebook.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(AxeItem.class)
public interface AxeAccessor {
    @Accessor("STRIPPABLES")
    static Map<Block, Block> getStripped() {
        throw new AssertionError("Untransformed Accessor");
    }
    @Accessor("STRIPPABLES")
    @Mutable
    static void setStripped(Map<Block, Block> stripped) {
        throw new AssertionError("Untransformed Accessor");
    }
}
