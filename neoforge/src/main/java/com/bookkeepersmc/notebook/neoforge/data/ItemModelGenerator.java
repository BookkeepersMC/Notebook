package com.bookkeepersmc.notebook.neoforge.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.FenceBlock;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.SaplingBlock;

import java.util.function.Supplier;

/**
 * An extension to {@link ItemModelProvider}.
 * Allows datagen to not use {@link Supplier}
 */
public abstract class ItemModelGenerator extends ItemModelProvider {
    @Nullable
    private final ResourceLocation name;

    public ItemModelGenerator(PackOutput output, String modid, ExistingFileHelper existingFileHelper, ResourceLocation name) {
        super(output, modid, existingFileHelper);
        this.name = name;
    }

    public ResourceLocation getId() {
        return this.name;
    }

    /**
     * Please COPY all of these methods for use!
     * Replace MOD_ID HERE with your MOD_ID.
     * Javadoc comments below show where to use most things.
     */
    public abstract static class Methods extends ItemModelGenerator {
        public Methods(PackOutput output, String modid, ExistingFileHelper existingFileHelper, ResourceLocation name) {
            super(output, modid, existingFileHelper, name);
        }

        /**
         * Use with {@link SaplingBlock}
         */
        public ItemModelBuilder sapling(Block item) {
            return withExistingParent(getId().getPath(),
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation("MOD_ID HERE", "block/" + getId().getPath()));
        }

        /**
         * Use with {@link Item}
         */
        private ItemModelBuilder simpleItem(Item item) {
            return withExistingParent(getId().getPath(),
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation("MOD_ID HERE", "item/" + getId().getPath()));
        }

        /**
         * Use with {@link FenceBlock}
         */
        public void fenceItem(Block block, Block baseBlock) {
            this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), mcLoc("block/fence_inventory"))
                    .texture("texture", new ResourceLocation("MOD_ID HERE", "block/" + BuiltInRegistries.BLOCK.getKey(baseBlock).getPath()));
        }

        /**
         * Use with {@link ButtonBlock}
         */
        public void buttonItem(Block block, Block baseBlock) {
            this.withExistingParent(BuiltInRegistries.BLOCK.getKey(block).getPath(), mcLoc("block/button_inventory"))
                    .texture("texture", new ResourceLocation("MOD_ID HERE", "block/" + BuiltInRegistries.BLOCK.getKey(baseBlock).getPath()));
        }

        /**
         * Use with {@link DoorBlock}
         */
        private ItemModelBuilder simpleBlockItem(Block item) {
            return withExistingParent(getId().getPath(),
                    new ResourceLocation("item/generated")).texture("layer0",
                    new ResourceLocation("MOD_ID here", "item/" + getId().getPath()));
        }
    }
}
