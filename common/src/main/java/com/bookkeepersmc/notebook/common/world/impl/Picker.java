/*
 * Copyright (c) 2023, 2024 BookkeepersMC under the MIT License
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.bookkeepersmc.notebook.common.world.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.synth.ImprovedNoise;
import com.google.common.base.Preconditions;

public final class Picker<T> {
    private double currentTotal;
    private final List<WeightedEntry<T>> entries;

    public Picker() {
        this(0, new ArrayList<>());
    }

    private Picker(double currentTotal, List<WeightedEntry<T>> entries) {
        this.currentTotal = currentTotal;
        this.entries = entries;
    }

    public void add(T biome, final double weight) {
        currentTotal += weight;

        entries.add(new WeightedEntry<>(biome, weight, currentTotal));
    }

    double getCurrentWeightTotal() {
        return currentTotal;
    }

    int getEntryCount() {
        return entries.size();
    }

    public T pickFromNoise(ImprovedNoise sampler, double x, double y, double z) {
        double target = Mth.clamp(Math.abs(sampler.noise(x, y, z)), 0, 1) * getCurrentWeightTotal();

        return search(target).entry();
    }

    <U> Picker<U> map(Function<T, U> mapper) {
        return new Picker<U>(
                currentTotal,
                entries.stream()
                        .map(e -> new WeightedEntry<>(mapper.apply(e.entry), e.weight, e.upperWeightBound))
                        .toList()
        );
    }

    WeightedEntry<T> search(final double target) {
        // Sanity checks, fail fast if stuff is going wrong.
        Preconditions.checkArgument(target <= currentTotal, "The provided target value for entry selection must be less than or equal to the weight total");
        Preconditions.checkArgument(target >= 0, "The provided target value for entry selection cannot be negative");

        int low = 0;
        int high = entries.size() - 1;

        while (low < high) {
            int mid = (high + low) >>> 1;

            if (target < entries.get(mid).upperWeightBound()) {
                high = mid;
            } else {
                low = mid + 1;
            }
        }

        return entries.get(low);
    }

    record WeightedEntry<T>(T entry, double weight, double upperWeightBound) {
    }
}
