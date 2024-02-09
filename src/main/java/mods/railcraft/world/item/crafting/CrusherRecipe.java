package mods.railcraft.world.item.crafting;

import java.util.Collections;
import java.util.List;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CrusherRecipe implements Recipe<Container> {
  private final Ingredient ingredient;
  private final List<CrusherOutput> probabilityOutputs;
  private final int processTime;

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
  public boolean matches(Container inventory, Level level) {
    return this.ingredient.test(inventory.getItem(0));
  }

  @Override
  public ItemStack assemble(Container inventory, RegistryAccess registryAccess) {
    return this.getResultItem(registryAccess).copy();
  }

  @Override
  public boolean canCraftInDimensions(int pWidth, int pHeight) {
    return true;
  }

  /**
   * Use {@link #getProbabilityOutputs()} since we have more output
   */
  @Override
  @Deprecated()
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  public List<CrusherOutput> getProbabilityOutputs() {
    return probabilityOutputs;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return NonNullList.of(Ingredient.EMPTY, ingredient);
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.CRUSHER.get();
  }

  @Override
  public RecipeType<?> getType() {
    return RailcraftRecipeTypes.CRUSHING.get();
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public ItemStack getToastSymbol() {
    return new ItemStack(RailcraftBlocks.CRUSHER.get());
  }

  public record CrusherOutput(Ingredient output, int quantity, double probability) {

    private static final Codec<CrusherOutput> CODEC = RecordCodecBuilder
        .create(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf(RecipeJsonKeys.RESULT)
                .forGetter(recipe -> recipe.output),
            ExtraCodecs.strictOptionalField(ExtraCodecs.POSITIVE_INT, RecipeJsonKeys.COUNT, 1)
                .forGetter(recipe -> recipe.quantity),
            Codec.doubleRange(0, 1).fieldOf(RecipeJsonKeys.PROBABILITY)
                .forGetter(recipe -> recipe.probability)
        ).apply(instance, CrusherOutput::new));

    public ItemStack getOutput() {
      return output.getItems()[0].copyWithCount(quantity);
    }
  }

  public static class Serializer implements RecipeSerializer<CrusherRecipe> {

    private static final Codec<CrusherRecipe> CODEC = RecordCodecBuilder
        .create(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf(RecipeJsonKeys.INGREDIENT)
                .forGetter(recipe -> recipe.ingredient),
            CrusherOutput.CODEC.listOf().fieldOf(RecipeJsonKeys.OUTPUTS)
                .orElse(Collections.emptyList())
                .forGetter(recipe -> recipe.probabilityOutputs),
            ExtraCodecs
                .strictOptionalField(ExtraCodecs.POSITIVE_INT, RecipeJsonKeys.PROCESS_TIME,
                    CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME)
                .forGetter(recipe -> recipe.processTime)
        ).apply(instance, CrusherRecipe::new));

    @Override
    public Codec<CrusherRecipe> codec() {
      return CODEC;
    }

    @Override
    public CrusherRecipe fromNetwork(FriendlyByteBuf buffer) {
      var tickCost = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var probabilityOutputs = buffer.readList(buf ->
          new CrusherOutput(Ingredient.fromNetwork(buf), buf.readVarInt(), buf.readDouble()));
      return new CrusherRecipe(ingredient, probabilityOutputs, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe) {
      buffer.writeVarInt(recipe.processTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeCollection(recipe.probabilityOutputs, (buf, item) -> {
        item.output.toNetwork(buf);
        buf.writeVarInt(item.quantity);
        buf.writeDouble(item.probability);
      });
    }
  }
}
