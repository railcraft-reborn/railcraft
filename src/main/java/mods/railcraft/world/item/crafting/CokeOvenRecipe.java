package mods.railcraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.material.RailcraftFluids;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleItemRecipe;
import net.neoforged.neoforge.fluids.FluidStack;

public class CokeOvenRecipe extends AbstractCookingRecipe {

  private static final MapCodec<CokeOvenRecipe> MAP_CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          Ingredient.CODEC.fieldOf(RecipeJsonKeys.INGREDIENT)
              .forGetter(SingleItemRecipe::input),
          ItemStackTemplate.CODEC.fieldOf(RecipeJsonKeys.RESULT)
              .forGetter(recipe -> recipe.result()),
          Codec.FLOAT.fieldOf(RecipeJsonKeys.EXPERIENCE)
              .orElse(0.0F)
              .forGetter(AbstractCookingRecipe::experience),
          ExtraCodecs.POSITIVE_INT.optionalFieldOf(RecipeJsonKeys.COOKING_TIME,
                  BlastFurnaceRecipeBuilder.DEFAULT_COOKING_TIME)
              .forGetter(AbstractCookingRecipe::cookingTime),
          ExtraCodecs.POSITIVE_INT.fieldOf(RecipeJsonKeys.CREOSOTE_OUTPUT)
              .forGetter(recipe -> recipe.creosoteOutput)
      ).apply(instance, CokeOvenRecipe::new));

  private static final StreamCodec<RegistryFriendlyByteBuf, CokeOvenRecipe> STREAM_CODEC =
      StreamCodec.of(CokeOvenRecipe::toNetwork, CokeOvenRecipe::fromNetwork);

  public static final RecipeSerializer<CokeOvenRecipe> SERIALIZER =
      new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  /**
   * Kept as a plain amount, as recipes are also built during data generation, where fluid
   * components are not bound yet and {@link FluidStack} cannot be created.
   */
  private final int creosoteOutput;

  public CokeOvenRecipe(Ingredient ingredient, ItemStackTemplate result,
      float experience, int cookingTime, int creosoteOutput) {
    super(new CommonInfo(true), new CookingBookInfo(CookingBookCategory.MISC, ""),
        ingredient, result, experience, cookingTime);
    this.creosoteOutput = creosoteOutput;
  }

  public FluidStack getCreosote() {
    return new FluidStack(RailcraftFluids.CREOSOTE.get(), this.creosoteOutput);
  }

  @Override
  public RecipeSerializer<CokeOvenRecipe> getSerializer() {
    return SERIALIZER;
  }

  @Override
  public RecipeType<CokeOvenRecipe> getType() {
    return RailcraftRecipeTypes.COKING.get();
  }

  @Override
  public RecipeBookCategory recipeBookCategory() {
    return RecipeBookCategories.CRAFTING_MISC;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  protected Item furnaceIcon() {
    return RailcraftBlocks.COKE_OVEN_BRICKS.get().asItem();
  }

  private static CokeOvenRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
    var creosoteOutput = buffer.readVarInt();
    var cookingTime = buffer.readVarInt();
    var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
    var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
    var experience = buffer.readFloat();
    return new CokeOvenRecipe(ingredient, result, experience, cookingTime, creosoteOutput);
  }

  private static void toNetwork(RegistryFriendlyByteBuf buffer, CokeOvenRecipe recipe) {
    buffer.writeVarInt(recipe.creosoteOutput);
    buffer.writeVarInt(recipe.cookingTime());
    Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input());
    ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result());
    buffer.writeFloat(recipe.experience());
  }
}
