package com.bookkeepersmc.notebook.common.object.builder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.mojang.datafixers.types.Type;

public class BlockEntityBuilder<T extends BlockEntity> {
    private final Factory<? extends T> factory;
    private final List<Block> blocks;

    private BlockEntityBuilder(Factory<? extends T> factory, List<Block> blocks) {
        this.factory = factory;
        this.blocks = blocks;
    }

    public static <T extends BlockEntity> BlockEntityBuilder<T> create(Factory<? extends T> factory, Block... blocks) {
        List<Block> blocksList = new ArrayList<>(blocks.length);
        Collections.addAll(blocksList, blocks);

        return new BlockEntityBuilder<>(factory, blocksList);
    }

    public BlockEntityBuilder<T> addBlock(Block block) {
        this.blocks.add(block);
        return this;
    }

    public BlockEntityBuilder<T> addBlocks(Block... blocks) {
        Collections.addAll(this.blocks, blocks);
        return this;
    }

    public BlockEntityType<T> build() {
        return build(null);
    }

    public BlockEntityType<T> build(Type<?> type) {
        return BlockEntityType.Builder.<T>of(factory::create, blocks.toArray(new Block[0])).build(type);
    }

    @FunctionalInterface
    public interface Factory<T extends BlockEntity> {
        T create(BlockPos blockPos, BlockState blockState);
    }
}
