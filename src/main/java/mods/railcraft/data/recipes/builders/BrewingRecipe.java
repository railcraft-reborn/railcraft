package mods.railcraft.data.recipes.builders;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.neoforged.neoforge.common.brewing.IBrewingRecipe;

public class BrewingRecipe implements IBrewingRecipe {

  private final Potion input, output;
  private final Item ingredient;

  public BrewingRecipe(Potion input, Item ingredient, Potion output) {
    this.input = input;
    this.ingredient = ingredient;
    this.output = output;
  }

  @Override
  public boolean isInput(ItemStack input) {
    return PotionUtils.getPotion(input).equals(this.input);
  }

  @Override
  public boolean isIngredient(ItemStack ingredient) {
    return ingredient.getItem().equals(this.ingredient);
  }

  @Override
  public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
    if (!this.isInput(input) || !this.isIngredient(ingredient)) {
      return ItemStack.EMPTY;
    }

    var itemStack = new ItemStack(input.getItem());
    itemStack.setTag(new CompoundTag());
    PotionUtils.setPotion(itemStack, this.output);
    return itemStack;
  }
}
