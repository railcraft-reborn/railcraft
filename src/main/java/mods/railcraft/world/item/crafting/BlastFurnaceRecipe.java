package mods.railcraft.world.item.crafting;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.BlastFurnaceRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
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

public class BlastFurnaceRecipe extends AbstractCookingRecipe {

  private static final MapCodec<BlastFurnaceRecipe> MAP_CODEC =
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
          ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf(RecipeJsonKeys.SLAG_OUTPUT, 0)
              .forGetter(recipe -> recipe.slagOutput)
      ).apply(instance, BlastFurnaceRecipe::new));

  private static final StreamCodec<RegistryFriendlyByteBuf, BlastFurnaceRecipe> STREAM_CODEC =
      StreamCodec.of(BlastFurnaceRecipe::toNetwork, BlastFurnaceRecipe::fromNetwork);

  public static final RecipeSerializer<BlastFurnaceRecipe> SERIALIZER =
      new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  private final int slagOutput;

  public BlastFurnaceRecipe(Ingredient input, ItemStackTemplate result,
      float experience, int cookingTime, int slagOutput) {
    super(new CommonInfo(true), new CookingBookInfo(CookingBookCategory.MISC, ""),
        input, result, experience, cookingTime);
    this.slagOutput = slagOutput;
  }

  public int getSlagOutput() {
    return this.slagOutput;
  }

  @Override
  public RecipeSerializer<BlastFurnaceRecipe> getSerializer() {
    return SERIALIZER;
  }

  @Override
  public RecipeType<BlastFurnaceRecipe> getType() {
    return RailcraftRecipeTypes.BLASTING.get();
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
    return RailcraftBlocks.BLAST_FURNACE_BRICKS.get().asItem();
  }

  private static BlastFurnaceRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
    var slagOutput = buffer.readVarInt();
    var cookingTime = buffer.readVarInt();
    var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
    var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
    var experience = buffer.readFloat();
    return new BlastFurnaceRecipe(ingredient, result, experience, cookingTime, slagOutput);
  }

  private static void toNetwork(RegistryFriendlyByteBuf buffer, BlastFurnaceRecipe recipe) {
    buffer.writeVarInt(recipe.slagOutput);
    buffer.writeVarInt(recipe.cookingTime());
    Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.input());
    ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result());
    buffer.writeFloat(recipe.experience());
  }
}
