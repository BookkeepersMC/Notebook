package dev.bookkeepersmc.notebook.registry.content;

import dev.bookkeepersmc.notebook.registry.content.impl.FlammableRegistryImpl;
import dev.bookkeepersmc.notebook.registry.content.util.BlockToObject;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public interface FlammableRegistry extends BlockToObject<FlammableRegistry.Entry> {
    static FlammableRegistry getDefaultInstance() {
        return getInstance(Blocks.FIRE);
    }

    static FlammableRegistry getInstance(Block block) {
        return FlammableRegistryImpl.getInstance(block);
    }

    default void add(Block block, int burn, int spread) {
        this.add(block, new Entry(burn, spread));
    }

    default void add(TagKey<Block> tag, int burn, int spread) {
        this.add(tag, new Entry(burn, spread));
    }


    final class Entry {
        private final int burn, spread;

        public Entry(int burn, int spread) {
            this.burn = burn;
            this.spread = spread;
        }

        public int getBurnChance() {
            return burn;
        }

        public int getSpreadChance() {
            return spread;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            } else {
                Entry other = (Entry) o;
                return other.burn == burn && other.spread == spread;
            }
        }

        @Override
        public int hashCode() {
            return burn * 11 + spread;
        }
    }
}
