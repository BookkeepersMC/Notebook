package dev.bookkeepersmc.notebook.mixin;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.behavior.GiveGiftToHero;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(GiveGiftToHero.class)
public interface GiveGiftsToHeroAccessor {
    @Accessor("GIFTS")
    static Map<VillagerProfession, ResourceLocation> getGifts() {
        throw new AssertionError("Untransformed Accessor!");
    }
}
