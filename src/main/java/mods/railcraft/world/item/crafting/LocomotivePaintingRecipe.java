package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.LocomotiveItem;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredItem;

public class LocomotivePaintingRecipe extends CustomRecipe {

  private static final LocomotivePaintingRecipe INSTANCE = new LocomotivePaintingRecipe();
  private static final MapCodec<LocomotivePaintingRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, LocomotivePaintingRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<LocomotivePaintingRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  /** Only used to show the recipe; any {@link LocomotiveItem} works. */
  private static final List<DeferredItem<LocomotiveItem>> LOCOMOTIVES = List.of(
      RailcraftItems.ELECTRIC_LOCOMOTIVE,
      RailcraftItems.STEAM_LOCOMOTIVE,
      RailcraftItems.CREATIVE_LOCOMOTIVE);

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
  public ItemStack assemble(CraftingInput craftingInput) {
    var dyePrimary = getItemStackInRow(craftingInput, 0);
    var loco = getItemStackInRow(craftingInput, 1);
    var dyeSecondary = getItemStackInRow(craftingInput, 2);

    if (!dyePrimary.has(DataComponents.DYE)) {
      return ItemStack.EMPTY;
    }
    if (!(loco.getItem() instanceof LocomotiveItem locomotiveItem)) {
      return ItemStack.EMPTY;
    }
    if (!dyeSecondary.has(DataComponents.DYE)) {
      return ItemStack.EMPTY;
    }

    var primaryColor = Objects.requireNonNull(dyePrimary.get(DataComponents.DYE));
    var secondaryColor = Objects.requireNonNull(dyeSecondary.get(DataComponents.DYE));
    var result = new ItemStack(locomotiveItem);
    var components = loco.getComponents();
    result.applyComponents(components);
    LocomotiveItem.setItemColorData(result, primaryColor, secondaryColor);
    return result;
  }

  @Override
  public List<RecipeDisplay> display() {
    var dyes = new SlotDisplay.TagSlotDisplay(ItemTags.DYES);
    var locomotives = new SlotDisplay.Composite(LOCOMOTIVES.stream()
        .<SlotDisplay>map(SlotDisplay.ItemSlotDisplay::new)
        .toList());
    // One dye above the locomotive and one below it
    return List.of(new ShapedCraftingRecipeDisplay(1, 3,
        List.of(dyes, locomotives, dyes),
        locomotives,
        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
  }

  @Override
  public RecipeSerializer<LocomotivePaintingRecipe> getSerializer() {
    return SERIALIZER;
  }
}
