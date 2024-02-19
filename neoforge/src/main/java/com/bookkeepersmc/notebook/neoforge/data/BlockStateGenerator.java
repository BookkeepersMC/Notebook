package com.bookkeepersmc.notebook.neoforge.data;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public abstract class BlockStateGenerator extends BlockStateProvider {
    public BlockStateGenerator(PackOutput output, String modid, ExistingFileHelper exFileHelper) {
        super(output, modid, exFileHelper);
    }
    /**
     * Please COPY all of these methods for use!
     * Replace MOD_ID with your MOD_ID.
     */
    public void blockItem(Block block, String appendix) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile("MOD_ID:block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + appendix));
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ResourceLocation texture) {
        ModelFile sign = models().sign(name(signBlock), texture);
        hangingSignBlock(signBlock, wallSignBlock, sign);
    }

    public void hangingSignBlock(Block signBlock, Block wallSignBlock, ModelFile sign) {
        simpleBlock(signBlock, sign);
        simpleBlock(wallSignBlock, sign);
    }

    private String name(Block block) {
        return key(block).getPath();
    }

    private ResourceLocation key(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    public void saplingBlock(Block block) {
        simpleBlock(block,
                models().cross(BuiltInRegistries.BLOCK.getKey(block).getPath(), blockTexture(block)).renderType("cutout"));
    }

    public void blockItem(Block block) {
        simpleBlockItem(block, new ModelFile.UncheckedModelFile("MOD_ID:block/" + BuiltInRegistries.BLOCK.getKey(block).getPath()));
    }

    public void blockWithItem(Block block) {
        simpleBlockWithItem(block, cubeAll(block));
    }
}
