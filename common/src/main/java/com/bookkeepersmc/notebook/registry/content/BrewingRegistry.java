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

package com.bookkeepersmc.notebook.registry.content;

import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;

public final class BrewingRegistry {
    private BrewingRegistry() {
    }

    /**
     * Register a recipe for brewing one potion type into another.
     * Only one recipe is necessary for all potions of the input type to be brewable into the output type using the ingredient.
     * @param input the input potion type (e.g. regular potion)
     * @param ingredient the required ingredient (e.g. gunpowder)
     * @param output the output type (e.g. splash potion)
     */
    public static void registerItemRecipe(PotionItem input, Ingredient ingredient, PotionItem output) {
        Objects.requireNonNull(input, "Input cannot be null!");
        Objects.requireNonNull(ingredient, "Ingredient cannot be null!");
        Objects.requireNonNull(output, "Output cannot be null!");
        PotionMaker.CONTAINER_MIX.add(new PotionMaker.Mixer<>(input, ingredient, output));
    }
    /**
     * Register a recipe for converting from one potion to another (e.g. awkward to instant health).
     * This does not automatically create long or strong versions of the output potion.
     * They require separate recipes.
     * @param input input potion (e.g. awkward)
     * @param ingredient the required ingredient (e.g. glistering melon)
     * @param output output potion (e.g. instant health)
     */
    public static void registerPotionRecipe(Potion input, Ingredient ingredient, Potion output) {
        Objects.requireNonNull(input, "Input cannot be null!");
        Objects.requireNonNull(ingredient, "Ingredient cannot be null!");
        Objects.requireNonNull(output, "Output cannot be null");
        PotionMaker.POTION_MIX.add(new PotionMaker.Mixer<>(input, ingredient, output));
    }
}
