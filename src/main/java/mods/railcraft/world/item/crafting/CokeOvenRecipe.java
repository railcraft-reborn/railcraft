package mods.railcraft.world.item.crafting;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import mods.railcraft.world.level.material.fluid.RailcraftFluids;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistryEntry;

public class CokeOvenRecipe implements IRecipe<IInventory> {

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
    return this.creosote.copy();
  }

  @Override
  public boolean matches(IInventory craftInventory, World world) {
    return this.recipeItem.test(craftInventory.getItem(0));
  }

  @Override
  public ItemStack assemble(IInventory craftInventory) {
    return this.result.copy();
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
  public IRecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.COKEING.get();
  }

  @Override
  public IRecipeType<?> getType() {
    return RailcraftRecipeTypes.COKEING;
  }

  public static class CokeOvenRecipeSerializer extends ForgeRegistryEntry<IRecipeSerializer<?>>
      implements IRecipeSerializer<CokeOvenRecipe> {

    @Override
    public CokeOvenRecipe fromJson(ResourceLocation resourceLoc, JsonObject jsonObject) {
      int tickCost = JSONUtils.getAsInt(jsonObject, "tickCost", 1000); // 1 bucket
      int creosoteOut = JSONUtils.getAsInt(jsonObject, "creosoteOut", 1000); // 1 bucket
      Ingredient ingredient =
          Ingredient.fromJson(JSONUtils.getAsJsonObject(jsonObject, "ingredient"));
      ItemStack resultItemStack =
          itemFromJson(JSONUtils.getAsJsonObject(jsonObject, "result"));
      // 3x3 recipies only, attempting to register 4x4's will not work and we will never honor it.
      return new CokeOvenRecipe(resourceLoc, creosoteOut, tickCost, ingredient, resultItemStack);
    }

    @Override
    public CokeOvenRecipe fromNetwork(ResourceLocation resourceLoc, PacketBuffer packetBuffer) {
      int creosoteOut = packetBuffer.readVarInt();
      int tickCost = packetBuffer.readVarInt();
      Ingredient ingredient = Ingredient.fromNetwork(packetBuffer);
      ItemStack itemstack = packetBuffer.readItem();

      return new CokeOvenRecipe(resourceLoc, creosoteOut, tickCost, ingredient, itemstack);
    }

    @Override
    public void toNetwork(PacketBuffer packetBuffer, CokeOvenRecipe recipe) {
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
