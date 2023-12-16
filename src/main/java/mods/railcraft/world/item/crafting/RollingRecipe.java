package mods.railcraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.Level;

public class RollingRecipe implements Recipe<CraftingContainer> {

  private final ShapedRecipePattern pattern;
  private final ItemStack result;
  private final int processTime;

  public RollingRecipe(ShapedRecipePattern pattern, ItemStack result, int processTime) {
    this.pattern = pattern;
    this.result = result;
    this.processTime = processTime;
  }

  /**
   * Get how long the user should wait before this gets crafted.
   *
   * @return tick cost, in int.
   */
  public int getProcessTime() {
    return this.processTime;
  }

  public int getWidth() {
    return this.pattern.width();
  }

  public int getHeight() {
    return this.pattern.height();
  }

  @Override
  public boolean matches(CraftingContainer inventory, Level level) {
    return this.pattern.matches(inventory);
  }

  @Override
  public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
    return this.getResultItem(registryAccess).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= this.pattern.width() && height >= this.pattern.height();
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return this.result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.pattern.ingredients();
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.ROLLING.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RailcraftRecipeTypes.ROLLING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.MANUAL_ROLLING_MACHINE.get());
  }

  public static class Serializer implements RecipeSerializer<RollingRecipe> {

    private static final Codec<RollingRecipe> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
            ItemStack.ITEM_WITH_COUNT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
            Codec.INT.fieldOf("processTime").orElse(RollingRecipeBuilder.DEFAULT_PROCESSING_TIME).forGetter(recipe -> recipe.processTime))
        .apply(instance, RollingRecipe::new)
    );

    @Override
    public Codec<RollingRecipe> codec() {
      return CODEC;
    }

    @Override
    public RollingRecipe fromNetwork(FriendlyByteBuf buffer) {
      var pattern = ShapedRecipePattern.fromNetwork(buffer);
      int processTime = buffer.readVarInt();
      var result = buffer.readItem();
      return new RollingRecipe(pattern, result, processTime);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RollingRecipe recipe) {
      recipe.pattern.toNetwork(buffer);
      buffer.writeVarInt(recipe.processTime);
      buffer.writeItem(recipe.result);
    }
  }
}
