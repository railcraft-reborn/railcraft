package mods.railcraft.world.item.crafting;

import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipeCodecs;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

public class RollingRecipe implements Recipe<CraftingContainer> {

  private final int width, height;
  private final NonNullList<Ingredient> ingredients;
  private final ItemStack result;
  private final int processTime;

  public RollingRecipe(int width, int height, NonNullList<Ingredient> ingredients,
      ItemStack result, int processTime) {
    this.width = width;
    this.height = height;
    this.ingredients = ingredients;
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
    return this.width;
  }

  public int getHeight() {
    return this.height;
  }

  @Override
  public boolean matches(CraftingContainer inventory, Level level) {
    for (int i = 0; i <= inventory.getWidth() - this.width; ++i) {
      for (int j = 0; j <= inventory.getHeight() - this.height; ++j) {
        if (this.matches(inventory, i, j, true)) {
          return true;
        }

        if (this.matches(inventory, i, j, false)) {
          return true;
        }
      }
    }

    return false;
  }

  private boolean matches(CraftingContainer inventory, int x, int y, boolean inverse) {
    for (int i = 0; i < inventory.getWidth(); ++i) {
      for (int j = 0; j < inventory.getHeight(); ++j) {
        int k = i - x;
        int l = j - y;
        Ingredient ingredient = Ingredient.EMPTY;
        if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
          if (inverse) {
            ingredient = this.ingredients.get(this.width - k - 1 + l * this.width);
          } else {
            ingredient = this.ingredients.get(k + l * this.width);
          }
        }

        if (!ingredient.test(inventory.getItem(i + j * inventory.getWidth()))) {
          return false;
        }
      }
    }

    return true;
  }

  @Override
  public ItemStack assemble(CraftingContainer inventory, RegistryAccess registryAccess) {
    return this.getResultItem(registryAccess).copy();
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width >= this.width && height >= this.height;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return this.result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    return this.ingredients;
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

    private static final Codec<RollingRecipe> CODEC = RollingRecipe.Serializer.RawRollingRecipe.CODEC
        .flatXmap(recipe -> {
      var patterns = ShapedRecipe.shrink(recipe.pattern);
      int width = patterns[0].length();
      int height = patterns.length;
      var ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
      var set = Sets.newHashSet(recipe.key.keySet());

      for(int k = 0; k < patterns.length; ++k) {
        String s = patterns[k];

        for(int l = 0; l < s.length(); ++l) {
          var s1 = s.substring(l, l + 1);
          var ingredient = s1.equals(" ") ? Ingredient.EMPTY : recipe.key.get(s1);
          if (ingredient == null) {
            return DataResult.error(() -> "Pattern references symbol '" + s1 + "' but it's not defined in the key");
          }

          set.remove(s1);
          ingredients.set(l + width * k, ingredient);
        }
      }

      if (!set.isEmpty()) {
        return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + set);
      } else {

        return DataResult.success(
            new RollingRecipe(width, height, ingredients, recipe.result, recipe.processTime));
      }
    }, recipe -> {
      throw new NotImplementedException("Serializing RollingRecipe is not implemented yet.");
    });

    @Override
    public Codec<RollingRecipe> codec() {
      return CODEC;
    }

    @Override
    public RollingRecipe fromNetwork(FriendlyByteBuf buffer) {
      int width = buffer.readVarInt();
      int height = buffer.readVarInt();
      int tickCost = buffer.readVarInt();
      var ingredients =
          buffer.readCollection(NonNullList::createWithCapacity, Ingredient::fromNetwork);
      var result = buffer.readItem();

      return new RollingRecipe(width, height, ingredients, result, tickCost);
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer, RollingRecipe recipe) {
      buffer.writeVarInt(recipe.width);
      buffer.writeVarInt(recipe.height);
      buffer.writeVarInt(recipe.processTime);
      buffer.writeCollection(recipe.ingredients, (buf, ingredient) -> ingredient.toNetwork(buffer));
      buffer.writeItem(recipe.result);
    }

    record RawRollingRecipe(Map<String, Ingredient> key, List<String> pattern,
                            ItemStack result, int processTime) {

      public static final Codec<RollingRecipe.Serializer.RawRollingRecipe> CODEC =
          RecordCodecBuilder.create(instance -> instance.group(
              ExtraCodecs.strictUnboundedMap(ShapedRecipe.Serializer.SINGLE_CHARACTER_STRING_CODEC, Ingredient.CODEC_NONEMPTY).fieldOf("key").forGetter(recipe -> recipe.key),
              ShapedRecipe.Serializer.PATTERN_CODEC.fieldOf("pattern").forGetter(recipe -> recipe.pattern),
              CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
              Codec.INT.fieldOf("processTime").orElse(RollingRecipeBuilder.DEFAULT_PROCESSING_TIME).forGetter(recipe -> recipe.processTime))
              .apply(instance, RollingRecipe.Serializer.RawRollingRecipe::new)
          );
    }
  }
}
