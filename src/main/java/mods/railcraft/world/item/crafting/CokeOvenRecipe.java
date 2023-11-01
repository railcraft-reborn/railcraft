package mods.railcraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.data.recipes.builders.CokeOvenRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.fluids.FluidStack;

public class CokeOvenRecipe extends AbstractCookingRecipe {

  private final FluidStack creosote;

  public CokeOvenRecipe(String group, Ingredient ingredient, ItemStack result,
      float experience, int cookingTime, int creosoteOutput) {
    super(RailcraftRecipeTypes.COKING.get(), group, CookingBookCategory.MISC,
        ingredient, result, experience, cookingTime);
    this.creosote = new FluidStack(RailcraftFluids.CREOSOTE.get(), creosoteOutput);
  }

  public FluidStack getCreosote() {
    return this.creosote;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.COKING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.COKE_OVEN_BRICKS.get());
  }

  public static class Serializer implements RecipeSerializer<CokeOvenRecipe> {

    private static final Codec<CokeOvenRecipe> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.strictOptionalField(
                Codec.STRING, "group", "").forGetter(recipe -> recipe.group),
                Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").forGetter(recipe -> recipe.result),
                Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(recipe -> recipe.experience),
                Codec.INT.fieldOf("cookingTime").orElse(CokeOvenRecipeBuilder.DEFAULT_COOKING_TIME).forGetter(recipe -> recipe.cookingTime),
                Codec.INT.fieldOf("creosoteOutput").orElse(1000).forGetter(recipe -> recipe.creosote.getAmount()))
            .apply(instance, CokeOvenRecipe::new));

    @Override
    public Codec<CokeOvenRecipe> codec() {
      return CODEC;
    }

    @Override
    public CokeOvenRecipe fromNetwork(FriendlyByteBuf buffer) {
      var group = buffer.readUtf();
      var creosoteOutput = buffer.readVarInt();
      var cookingTime = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var result = buffer.readItem();
      var experience = buffer.readFloat();
      return new CokeOvenRecipe(group, ingredient, result, experience, cookingTime, creosoteOutput);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CokeOvenRecipe recipe) {
      buffer.writeUtf(recipe.group);
      buffer.writeVarInt(recipe.creosote.getAmount());
      buffer.writeVarInt(recipe.cookingTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeItem(recipe.result);
      buffer.writeFloat(recipe.experience);
    }
  }
}
