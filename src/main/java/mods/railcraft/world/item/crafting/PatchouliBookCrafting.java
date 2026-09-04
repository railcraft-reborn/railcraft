package mods.railcraft.world.item.crafting;

import java.util.List;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import vazkii.patchouli.api.PatchouliAPI;

public class PatchouliBookCrafting extends ShapelessRecipe {

  private static final PatchouliBookCrafting INSTANCE = new PatchouliBookCrafting();
  private static final MapCodec<ShapelessRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, ShapelessRecipe> STREAM_CODEC =
      StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<ShapelessRecipe> SERIALIZER =
      new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  private static final Identifier BOOK_ID = RailcraftConstants.id("guide_book");

  public PatchouliBookCrafting() {
    super(new Recipe.CommonInfo(true),
        new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, ""),
        null,
        List.of(Ingredient.of(Items.BOOK), Ingredient.of(RailcraftItems.IRON_CROWBAR.get())));
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput) {
    return PatchouliAPI.get().getBookStack(BOOK_ID);
  }

  @Override
  public List<RecipeDisplay> display() {
    return List.of(new ShapelessCraftingRecipeDisplay(
        this.placementInfo().ingredients().stream().map(Ingredient::display).toList(),
        new SlotDisplay.ItemStackSlotDisplay(PatchouliAPI.get().getBookStackTemplate(BOOK_ID)),
        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
  }

  @Override
  public RecipeSerializer<ShapelessRecipe> getSerializer() {
    return SERIALIZER;
  }
}
