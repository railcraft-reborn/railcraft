package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.util.RecipeUtil;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class CrusherRecipe implements Recipe<SingleRecipeInput> {
  private final Ingredient ingredient;
  private final List<CrusherOutput> probabilityOutputs;
  private final int processTime;
  @Nullable
  private PlacementInfo placementInfo;

  public CrusherRecipe(Ingredient ingredient,
      List<CrusherOutput> probabilityOutputs, int processTime) {
    this.ingredient = ingredient;
    this.probabilityOutputs = probabilityOutputs;
    this.processTime = processTime;
  }

  public int getProcessTime() {
    return this.processTime;
  }

  @Override
  public boolean matches(SingleRecipeInput inventory, Level level) {
    return this.ingredient.test(inventory.getItem(0));
  }

  @Override
  public ItemStack assemble(SingleRecipeInput inventory, HolderLookup.Provider provider) {
    return ItemStack.EMPTY;
  }

  public List<CrusherOutput> getProbabilityOutputs() {
    return probabilityOutputs;
  }

  @Override
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      this.placementInfo = PlacementInfo.create(ingredient);
    }
    return this.placementInfo;
  }

  @Override
  public RecipeSerializer<CrusherRecipe> getSerializer() {
    return RailcraftRecipeSerializers.CRUSHER.get();
  }

  @Override
  public RecipeType<CrusherRecipe> getType() {
    return RailcraftRecipeTypes.CRUSHING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public RecipeBookCategory recipeBookCategory() {
    return RecipeBookCategories.CRAFTING_MISC;
  }

  public record CrusherOutput(Ingredient output, int quantity, double probability) {

    private static final Codec<CrusherOutput> CODEC = RecordCodecBuilder
        .create(instance -> instance.group(
            Ingredient.CODEC.fieldOf(RecipeJsonKeys.RESULT)
                .forGetter(recipe -> recipe.output),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf(RecipeJsonKeys.COUNT, 1)
                .forGetter(recipe -> recipe.quantity),
            Codec.doubleRange(0, 1).fieldOf(RecipeJsonKeys.PROBABILITY)
                .forGetter(recipe -> recipe.probability)
        ).apply(instance, CrusherOutput::new));

    public ItemStack getOutput() {
      return RecipeUtil.getPreferredStackByMod(output.getValues().stream().toList()).copyWithCount(quantity);
    }
  }

  public static class Serializer implements RecipeSerializer<CrusherRecipe> {

    private static final MapCodec<CrusherRecipe> CODEC =
        RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf(RecipeJsonKeys.INGREDIENT)
                .forGetter(recipe -> recipe.ingredient),
            CrusherOutput.CODEC.listOf().fieldOf(RecipeJsonKeys.OUTPUTS)
                .orElse(Collections.emptyList())
                .forGetter(recipe -> recipe.probabilityOutputs),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf(RecipeJsonKeys.PROCESS_TIME,
                    CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME)
                .forGetter(recipe -> recipe.processTime)
        ).apply(instance, CrusherRecipe::new));

    private static final StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> STREAM_CODEC =
        StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

    @Override
    public MapCodec<CrusherRecipe> codec() {
      return CODEC;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, CrusherRecipe> streamCodec() {
      return STREAM_CODEC;
    }

    private static CrusherRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
      var tickCost = buffer.readVarInt();
      var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
      var size = buffer.readVarInt();
      var probabilityOutputs = new ArrayList<CrusherOutput>();
      for (int i = 0; i < size; i++) {
        probabilityOutputs.add(new CrusherOutput(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer),
            buffer.readVarInt(), buffer.readDouble()));
      }
      return new CrusherRecipe(ingredient, probabilityOutputs, tickCost);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, CrusherRecipe recipe) {
      buffer.writeVarInt(recipe.processTime);
      Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.ingredient);
      buffer.writeVarInt(recipe.probabilityOutputs.size());
      for (int i = 0; i < recipe.probabilityOutputs.size(); i++) {
        var item = recipe.probabilityOutputs.get(i);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, item.output);
        buffer.writeVarInt(item.quantity);
        buffer.writeDouble(item.probability);
      }
    }
  }
}
