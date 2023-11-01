package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.data.recipes.builders.CrusherRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CrusherRecipe implements Recipe<Container> {
  private final Ingredient ingredient;
  private final List<Pair<ItemStack, Double>> probabilityItems;
  private final int processTime;

  /**
   * Creates a new recipe.
   *
   * @param ingredient       - Ingredient of the object
   * @param probabilityItems - A list represents ItemStack - Probability
   * @param processTime      - The time cost of the recipe
   */
  public CrusherRecipe(Ingredient ingredient,
      List<Pair<ItemStack, Double>> probabilityItems, int processTime) {
    this.ingredient = ingredient;
    this.probabilityItems = probabilityItems;
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
   * Use {@link #getProbabilityItems()} since we have more output
   */
  @Override
  @Deprecated(forRemoval = false)
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return ItemStack.EMPTY;
  }

  public List<Pair<ItemStack, Double>> getProbabilityItems() {
    return probabilityItems;
  }

  public List<ItemStack> pollOutputs(RandomSource random) {
    var result = new ArrayList<ItemStack>();
    for(var item : probabilityItems) {
      if(random.nextDouble() < item.getSecond()) {
        result.add(item.getFirst().copy());
      }
    }
    return result;
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

  public static class Serializer implements RecipeSerializer<CrusherRecipe> {

    private static final Codec<List<Pair<ItemStack, Double>>> LIST_CODEC = Codec.compoundList(
        BuiltInRegistries.ITEM.byNameCodec().xmap(ItemStack::new, ItemStack::getItem).fieldOf("result").codec(),
        Codec.doubleRange(0, 1).fieldOf("probability").codec());

    private static final Codec<CrusherRecipe> CODEC =
        RecordCodecBuilder.create(instance -> instance.group(
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
            LIST_CODEC.fieldOf("outputs").forGetter(crusherRecipe -> crusherRecipe.probabilityItems),
            Codec.INT.fieldOf("processTime").orElse(CrusherRecipeBuilder.DEFAULT_PROCESSING_TIME).forGetter(recipe -> recipe.processTime))
            .apply(instance, CrusherRecipe::new));

    @Override
    public Codec<CrusherRecipe> codec() {
      return CODEC;
    }

    @Override
    public CrusherRecipe fromNetwork(FriendlyByteBuf buffer) {
      var tickCost = buffer.readVarInt();
      var ingredient = Ingredient.fromNetwork(buffer);
      var probabilityItems = buffer.readList(buf -> {
        var result = buf.readItem();
        var probability = buf.readDouble();
        return new Pair<>(result, probability);
      });
      return new CrusherRecipe(ingredient, probabilityItems, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, CrusherRecipe recipe) {
      buffer.writeVarInt(recipe.processTime);
      recipe.ingredient.toNetwork(buffer);
      buffer.writeCollection(recipe.probabilityItems, (buf, item) -> {
        buf.writeItem(item.getFirst());
        buf.writeDouble(item.getSecond());
      });
    }
  }
}
