package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class LocomotivePaintingRecipe extends CustomRecipe {

  public LocomotivePaintingRecipe(ResourceLocation id, CraftingBookCategory category) {
    super(id, category);
  }

  private ItemStack getItemStackInRow(CraftingContainer container, int row) {
    int width = container.getWidth();
    var result = new ArrayList<ItemStack>();
    for (int i = 0; i < container.getWidth(); i++) {
      var item = container.getItem(row * width + i);
      if (!item.isEmpty()) {
        result.add(item);
      }
    }
    return result.size() != 1 ? ItemStack.EMPTY : result.get(0);
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    var dyePrimary = getItemStackInRow(container, 0);
    if (!(dyePrimary.getItem() instanceof DyeItem))
      return false;
    var loco = getItemStackInRow(container, 1);
    if (!(loco.getItem() instanceof LocomotiveItem))
      return false;
    var dyeSecondary = getItemStackInRow(container, 2);
    return dyeSecondary.getItem() instanceof DyeItem;
  }

  @Override
  public ItemStack assemble(CraftingContainer container) {
    var dyePrimary = getItemStackInRow(container, 0);
    var loco = getItemStackInRow(container, 1);
    var dyeSecondary = getItemStackInRow(container, 2);

    if (!(dyePrimary.getItem() instanceof DyeItem primaryItem)) {
      return ItemStack.EMPTY;
    }
    if (!(loco.getItem() instanceof LocomotiveItem locomotiveItem)) {
      return ItemStack.EMPTY;
    }
    if (!(dyeSecondary.getItem() instanceof DyeItem secondaryItem)) {
      return ItemStack.EMPTY;
    }

    var primaryColor = primaryItem.getDyeColor();
    var secondaryColor = secondaryItem.getDyeColor();
    var result = new ItemStack(locomotiveItem);
    var tag = loco.getTag();
    if (tag != null) {
      result.setTag(tag);
    }
    LocomotiveItem.setItemColorData(result, primaryColor, secondaryColor);
    return result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    var ingredients = NonNullList.withSize(9, Ingredient.EMPTY);
    ingredients.set(1, Ingredient.of(Tags.Items.DYES));
    ingredients.set(4, Ingredient.of(RailcraftItems.STEAM_LOCOMOTIVE.get()));
    ingredients.set(7, Ingredient.of(Tags.Items.DYES));
    return ingredients;
  }

  @Override
  public ItemStack getResultItem() {
    return new ItemStack(RailcraftItems.STEAM_LOCOMOTIVE.get());
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= 1 && height >= 3;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.LOCOMOTIVE_PAINTING.get();
  }
}
