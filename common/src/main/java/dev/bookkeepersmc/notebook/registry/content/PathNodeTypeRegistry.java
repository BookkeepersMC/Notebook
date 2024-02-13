package dev.bookkeepersmc.notebook.registry.content;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class PathNodeTypeRegistry {
    private static final Logger LOGGER = LoggerFactory.getLogger(PathNodeTypeRegistry.class);
    private static final Map<Block, PathNodeProvider> NODE_TYPES = new IdentityHashMap<>();

    private PathNodeTypeRegistry() {
    }

    public static void register(Block block, @Nullable BlockPathTypes nodeType, @Nullable BlockPathTypes nodeTypeIfNeighbor) {
        Objects.requireNonNull(block, "Block cannot be null!");

        // Registers a provider that always returns the specified node type.
        register(block, (state, neighbor) -> neighbor ? nodeTypeIfNeighbor : nodeType);
    }

    public static void register(Block block, StaticPathNodeProvider provider) {
        Objects.requireNonNull(block, "Block cannot be null!");
        Objects.requireNonNull(provider, "StaticPathNodeProvider cannot be null!");

        // Registers the provider.
        PathNodeProvider old = NODE_TYPES.put(block, provider);

        if (old != null) {
            LOGGER.debug("Replaced PathNodeType provider for the block {}", block);
        }
    }


    public static void registerDynamic(Block block, DynamicPathNodeProvider provider) {
        Objects.requireNonNull(block, "Block cannot be null!");
        Objects.requireNonNull(provider, "DynamicPathNodeProvider cannot be null!");

        // Registers the provider.
        PathNodeProvider old = NODE_TYPES.put(block, provider);

        if (old != null) {
            LOGGER.debug("Replaced PathNodeType provider for the block {}", block);
        }
    }


    @Nullable
    public static BlockPathTypes getPathNodeType(BlockState state, BlockGetter world, BlockPos pos, boolean neighbor) {
        Objects.requireNonNull(state, "BlockState cannot be null!");
        Objects.requireNonNull(world, "BlockView cannot be null!");
        Objects.requireNonNull(pos, "BlockPos cannot be null!");

        // Gets the node type provider for the block.
        PathNodeProvider provider = getPathNodeTypeProvider(state.getBlock());

        //If no provider exists, returns null.
        if (provider == null) return null;

        //If a provider exists, returns the node type obtained from the provider.
        //The node type can be null too.
        if (provider instanceof DynamicPathNodeProvider) {
            return ((DynamicPathNodeProvider) provider).getPathNodeType(state, world, pos, neighbor);
        } else {
            return ((StaticPathNodeProvider) provider).getPathNodeType(state, neighbor);
        }
    }


    @Nullable
    public static PathNodeProvider getPathNodeTypeProvider(Block block) {
        Objects.requireNonNull(block, "Block cannot be null!");

        return NODE_TYPES.get(block);
    }


    public sealed interface PathNodeProvider permits StaticPathNodeProvider, DynamicPathNodeProvider {
    }


    @FunctionalInterface
    public non-sealed interface StaticPathNodeProvider extends PathNodeProvider {
        @Nullable
        BlockPathTypes getPathNodeType(BlockState state, boolean neighbor);
    }


    @FunctionalInterface
    public non-sealed interface DynamicPathNodeProvider extends PathNodeProvider {
        @Nullable
        BlockPathTypes getPathNodeType(BlockState state, BlockGetter world, BlockPos pos, boolean neighbor);
    }
}
