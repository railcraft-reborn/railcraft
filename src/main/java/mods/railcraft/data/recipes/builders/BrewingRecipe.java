package mods.railcraft.data.recipes.builders;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

public class BrewingRecipe {

  private final Holder<Potion> input, output;
  private final Item ingredient;

  public BrewingRecipe(Holder<Potion> input, Item ingredient, Holder<Potion> output) {
    this.input = input;
    this.ingredient = ingredient;
    this.output = output;
  }

  public boolean isInput(ItemStack input) {
    var potionContent = input.get(DataComponents.POTION_CONTENTS);
    if (potionContent != null) {
      return potionContent.is(this.input);
    }
    return false;
  }

  public boolean isIngredient(ItemStack ingredient) {
    return ingredient.getItem().equals(this.ingredient);
  }

  public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
    if (!this.isInput(input) || !this.isIngredient(ingredient)) {
      return ItemStack.EMPTY;
    }

    var itemStack = new ItemStack(input.getItem());
    itemStack.set(DataComponents.POTION_CONTENTS, new PotionContents(this.output));
    return itemStack;
  }
}
