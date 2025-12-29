package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.LocomotiveColorComponent;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

public class LocomotivePaintingRecipe extends CustomRecipe {

  @Nullable
  private PlacementInfo placementInfo;
  protected final NonNullList<Optional<Ingredient>> ingredients;

  public LocomotivePaintingRecipe(CraftingBookCategory category) {
    super(category);
    this.ingredients = NonNullList.withSize(9, Optional.empty());
    this.ingredients.set(1, Optional.of(Ingredient.of(Items.RED_DYE)));
    this.ingredients.set(4, Optional.of(Ingredient.of(RailcraftItems.STEAM_LOCOMOTIVE.get())));
    this.ingredients.set(7, Optional.of(Ingredient.of(Items.BLUE_DYE)));
  }

  private ItemStack getItemStackInRow(CraftingInput craftingInput, int row) {
    int width = craftingInput.width();
    var result = new ArrayList<ItemStack>();
    for (int i = 0; i < craftingInput.width(); i++) {
      var item = craftingInput.getItem(row * width + i);
      if (!item.isEmpty()) {
        result.add(item);
      }
    }
    return result.size() != 1 ? ItemStack.EMPTY : result.getFirst();
  }

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    if (craftingInput.height() < 3)
      return false;
    var dyePrimary = getItemStackInRow(craftingInput, 0);
    if (!(dyePrimary.getItem() instanceof DyeItem))
      return false;
    var loco = getItemStackInRow(craftingInput, 1);
    if (!(loco.getItem() instanceof LocomotiveItem))
      return false;
    var dyeSecondary = getItemStackInRow(craftingInput, 2);
    return dyeSecondary.getItem() instanceof DyeItem;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput, HolderLookup.Provider provider) {
    var dyePrimary = getItemStackInRow(craftingInput, 0);
    var loco = getItemStackInRow(craftingInput, 1);
    var dyeSecondary = getItemStackInRow(craftingInput, 2);

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
    var components = loco.getComponents();
    result.applyComponents(components);
    LocomotiveItem.setItemColorData(result, primaryColor, secondaryColor);
    return result;
  }

  @Override
  public RecipeSerializer<LocomotivePaintingRecipe> getSerializer() {
    return RailcraftRecipeSerializers.LOCOMOTIVE_PAINTING.get();
  }

  @Override
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      this.placementInfo = PlacementInfo.createFromOptionals(this.ingredients);
    }
    return this.placementInfo;
  }

  @Override
  public List<RecipeDisplay> display() {
    var result = RailcraftItems.STEAM_LOCOMOTIVE.toStack();
    result.set(RailcraftDataComponents.LOCOMOTIVE_COLOR.get(),
        new LocomotiveColorComponent(DyeColor.RED, DyeColor.BLUE));

    return List.of(
        new ShapedCraftingRecipeDisplay(
            3,
            3,
            this.ingredients.stream()
                .map(i -> i.map(Ingredient::display)
                    .orElse(SlotDisplay.Empty.INSTANCE))
                .toList(),
            new SlotDisplay.ItemStackSlotDisplay(result),
            new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
        )
    );
  }
}
