package dev.bookkeepersmc.notebook.mixin;

import net.minecraft.world.entity.ai.behavior.WorkAtComposter;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(WorkAtComposter.class)
public interface FarmerAccessor {
    @Mutable
    @Accessor("COMPOSTABLE_ITEMS")
    static void setCompostables(List<Item> items) {
        throw new AssertionError("Untransformed Accessor!");
    }
    @Mutable
    @Accessor("COMPOSTABLE_ITEMS")
    static List<Item> getCompostables() {
        throw new AssertionError("Untransformed Accessor!");
    }
}
