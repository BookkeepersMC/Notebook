package com.bookkeepersmc.notebook.common.world.impl;

import net.minecraft.world.level.levelgen.synth.ImprovedNoise;

public interface SamplerHooks {
    ImprovedNoise getEndBiomesSampler();

    void setSeed(long seed);

    long getSeed();
}
