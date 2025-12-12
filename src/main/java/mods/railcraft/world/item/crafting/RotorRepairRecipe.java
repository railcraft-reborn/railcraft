package mods.railcraft.world.item.crafting;

import java.util.stream.IntStream;
import org.jspecify.annotations.Nullable;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class RotorRepairRecipe extends CustomRecipe {

  public static final int REPAIR_PER_BLADE = 2500;

  private static final Ingredient ROTOR = Ingredient.of(RailcraftItems.TURBINE_ROTOR.get());
  private static final Ingredient BLADE = Ingredient.of(RailcraftItems.TURBINE_BLADE.get());
  @Nullable
  private PlacementInfo placementInfo;

  public RotorRepairRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    if (craftingInput.height() < 2 || craftingInput.width() < 2) {
      return false;
    }

    boolean containsRotor = false;
    boolean containsBlade = false;
    for (int i = 0; i < craftingInput.size(); i++) {
      var stack = craftingInput.getItem(i);
      if (!containsRotor && ROTOR.test(stack)) {
        containsRotor = true;
      }
      if (!containsBlade && BLADE.test(stack)) {
        containsBlade = true;
      }
    }
    return containsRotor && containsBlade;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var rotor = IntStream.range(0, craftingInput.size())
        .mapToObj(craftingInput::getItem)
        .filter(ROTOR)
        .findFirst()
        .orElse(ItemStack.EMPTY);
    if(rotor.isEmpty()) {
      return ItemStack.EMPTY;
    }
    var numBlades = ((int) IntStream.range(0, craftingInput.size())
        .mapToObj(craftingInput::getItem)
        .filter(BLADE)
        .count());

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
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      NonNullList<Ingredient> ingredients = NonNullList.create();
      ingredients.add(ROTOR);
      ingredients.add(BLADE);
      this.placementInfo = PlacementInfo.create(ingredients);
    }
    return this.placementInfo;
  }

  @Override
  public RecipeSerializer<RotorRepairRecipe> getSerializer() {
    return RailcraftRecipeSerializers.ROTOR_REPAIR.get();
  }
}
