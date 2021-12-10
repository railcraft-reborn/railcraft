package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CokeOvenRecipe implements Recipe<Container> {

  private final ResourceLocation id;
  private final Ingredient recipeItem;
  private final ItemStack result;
  private final int tickCost;
  private final FluidStack creosote;

  /**
   * Creates a new recipie.
   * @param resourceLocation -
   * @param creosoteOut - How many creosote does this recipie produce. In Milibuckets
   * @param ingredient - Ingredients list of the object
   * @param resultItemStack - The result
   */
  public CokeOvenRecipe(ResourceLocation resourceLocation, int creosoteOut, int tickCost,
      Ingredient ingredient, ItemStack resultItemStack) {
    this.id = resourceLocation;
    this.recipeItem = ingredient;
    this.result = resultItemStack;
    this.tickCost = tickCost;
    this.creosote = new FluidStack(RailcraftFluids.CREOSOTE.get(), creosoteOut);
  }

  public int getTickCost() {
    return tickCost;
  }

  // TODO: create our own IFluidRecipie interface
  public FluidStack getResultFluid() {
    return this.creosote;
  }

  public FluidStack assembleFluid() {
    return this.getResultFluid().copy();
  }

  @Override
  public boolean matches(Container craftInventory, Level world) {
    return this.recipeItem.test(craftInventory.getItem(0));
  }

  @Override
  public ItemStack assemble(Container craftInventory) {
    return this.getResultItem().copy();
  }

  @Override
  public boolean canCraftInDimensions(int x, int y) {
    return true;
  }

  @Override
  public ItemStack getResultItem() {
    return this.result;
  }

  @Override
  public ResourceLocation getId() {
    return this.id;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.COKE_OVEN_COOKING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RailcraftRecipeTypes.COKE_OVEN_COOKING;
  }

  public static class CokeOvenRecipeSerializer extends ForgeRegistryEntry<RecipeSerializer<?>>
      implements RecipeSerializer<CokeOvenRecipe> {

    @Override
    public CokeOvenRecipe fromJson(ResourceLocation resourceLoc, JsonObject jsonObject) {
      int tickCost = GsonHelper.getAsInt(jsonObject, "tickCost", 1000); // 50 sec
      int creosoteOut = GsonHelper.getAsInt(jsonObject, "creosoteOut", 1000); // 1 bucket
      Ingredient ingredient =
          Ingredient.fromJson(GsonHelper.getAsJsonObject(jsonObject, "ingredient"));
      ItemStack resultItemStack =
          itemFromJson(GsonHelper.getAsJsonObject(jsonObject, "result"));

      return new CokeOvenRecipe(resourceLoc, creosoteOut, tickCost, ingredient, resultItemStack);
    }

    @Override
    public CokeOvenRecipe fromNetwork(ResourceLocation resourceLoc, FriendlyByteBuf packetBuffer) {
      int creosoteOut = packetBuffer.readVarInt();
      int tickCost = packetBuffer.readVarInt();
      Ingredient ingredient = Ingredient.fromNetwork(packetBuffer);
      ItemStack itemstack = packetBuffer.readItem();

      return new CokeOvenRecipe(resourceLoc, creosoteOut, tickCost, ingredient, itemstack);
    }

    @Override
    public void toNetwork(FriendlyByteBuf packetBuffer, CokeOvenRecipe recipe) {
      packetBuffer.writeVarInt(recipe.creosote.getAmount());
      packetBuffer.writeVarInt(recipe.tickCost);
      recipe.recipeItem.toNetwork(packetBuffer);
      packetBuffer.writeItem(recipe.result);
    }

    public static final ItemStack itemFromJson(JsonObject jsondat) {
      if (!jsondat.has("item")) {
        throw new JsonParseException("No item key found");
      }
      if (jsondat.has("data")) {
        throw new JsonParseException("Disallowed data tag found");
      } else {
        return net.minecraftforge.common.crafting.CraftingHelper.getItemStack(jsondat, true);
      }
    }
  }
}
