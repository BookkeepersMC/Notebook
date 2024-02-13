package dev.bookkeepersmc.notebook.registry.content;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
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
        PotionBrewing.CONTAINER_MIXES.add(new PotionBrewing.Mix<>(input, ingredient, output));
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
        PotionBrewing.POTION_MIXES.add(new PotionBrewing.Mix<>(input, ingredient, output));
    }
}
