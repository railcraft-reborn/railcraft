package mods.railcraft.world.item.crafting;

import java.util.stream.IntStream;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RotorRepairRecipe extends CustomRecipe {

  public static final int REPAIR_PER_BLADE = 2500;

  private static final Ingredient ROTOR = Ingredient.of(RailcraftItems.TURBINE_ROTOR.get());
  private static final Ingredient BLADE = Ingredient.of(RailcraftItems.TURBINE_BLADE.get());

  public RotorRepairRecipe(ResourceLocation id, CraftingBookCategory category) {
    super(id, category);
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    return container.hasAnyMatching(ROTOR) && container.hasAnyMatching(BLADE);
  }

  @Override
  public ItemStack assemble(CraftingContainer container) {
    var rotor = IntStream.range(0, container.getContainerSize())
        .mapToObj(container::getItem)
        .filter(ROTOR)
        .findFirst()
        .orElse(ItemStack.EMPTY);
    if(rotor.isEmpty()) {
      return ItemStack.EMPTY;
    }
    var numBlades = IntStream.range(0, container.getContainerSize())
        .mapToObj(container::getItem)
        .filter(BLADE)
        .count();

    int damage = rotor.getDamageValue();
    damage -= REPAIR_PER_BLADE * numBlades;
    if (damage < 0) {
      damage = 0;
    }
    var result = rotor.copy();
    result.setDamageValue(damage);
    return result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> ingredients = NonNullList.create();
    ingredients.add(ROTOR);
    ingredients.add(BLADE);
    return ingredients;
  }

  @Override
  public ItemStack getResultItem() {
    return new ItemStack(RailcraftItems.TURBINE_ROTOR.get());
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= 2 && height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.ROTOR_REPAIR.get();
  }
}
