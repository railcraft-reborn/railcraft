package mods.railcraft.world.item.crafting;

import java.util.stream.IntStream;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

public abstract class CartDisassemblyRecipe extends CustomRecipe {

  private final Item ingredient;
  private final Item result;

  public CartDisassemblyRecipe(ResourceLocation id, Item ingredient, Item result,
      CraftingBookCategory category) {
    super(id, category);
    this.ingredient = ingredient;
    this.result = result;
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    var items = IntStream.range(0, container.getContainerSize())
        .mapToObj(container::getItem)
        .filter(x -> !x.isEmpty())
        .count();
    return items == 1 && container.hasAnyMatching(x -> x.is(this.ingredient));
  }

  @Override
  public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
    return this.getResultItem(registryAccess).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= 1 && height >= 1;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return new ItemStack(this.result);
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> ingredients = NonNullList.create();
    ingredients.add(Ingredient.of(this.ingredient));
    return ingredients;
  }

  @Override
  public NonNullList<ItemStack> getRemainingItems(CraftingContainer container) {
    var grid = NonNullList.withSize(container.getContainerSize(), ItemStack.EMPTY);
    for (int i = 0; i < container.getContainerSize(); i++) {
      var itemStack = container.getItem(i);
      if (itemStack.is(this.ingredient)) {
        grid.set(i, new ItemStack(Items.MINECART));
      }
    }
    return grid;
  }
}
