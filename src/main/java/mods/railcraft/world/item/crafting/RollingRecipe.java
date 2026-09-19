package mods.railcraft.world.item.crafting;

import java.util.List;
import org.jspecify.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mods.railcraft.api.core.RecipeJsonKeys;
import mods.railcraft.data.recipes.builders.RollingRecipeBuilder;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;

public class RollingRecipe implements Recipe<CraftingInput> {

  private static final MapCodec<RollingRecipe> MAP_CODEC =
      RecordCodecBuilder.mapCodec(instance -> instance.group(
          ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
          ItemStackTemplate.CODEC.fieldOf(RecipeJsonKeys.RESULT)
              .forGetter(recipe -> recipe.result),
          ExtraCodecs.POSITIVE_INT.optionalFieldOf(RecipeJsonKeys.PROCESS_TIME,
                  RollingRecipeBuilder.DEFAULT_PROCESSING_TIME)
              .forGetter(recipe -> recipe.processTime)
      ).apply(instance, RollingRecipe::new));

  private static final StreamCodec<RegistryFriendlyByteBuf, RollingRecipe> STREAM_CODEC =
      StreamCodec.of(RollingRecipe::toNetwork, RollingRecipe::fromNetwork);

  public static final RecipeSerializer<RollingRecipe> SERIALIZER =
      new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  private final ShapedRecipePattern pattern;
  private final ItemStackTemplate result;
  private final int processTime;
  @Nullable
  private PlacementInfo placementInfo;

  public RollingRecipe(ShapedRecipePattern pattern, ItemStackTemplate result, int processTime) {
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
  public boolean matches(CraftingInput inventory, Level level) {
    return this.pattern.matches(inventory);
  }

  @Override
  public ItemStack assemble(CraftingInput inventory) {
    return this.result.create();
  }

  @Override
  public RecipeSerializer<RollingRecipe> getSerializer() {
    return SERIALIZER;
  }

  @Override
  public RecipeType<RollingRecipe> getType() {
    return RailcraftRecipeTypes.ROLLING.get();
  }

  @Override
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      this.placementInfo = PlacementInfo.createFromOptionals(this.pattern.ingredients());
    }
    return this.placementInfo;
  }

  @Override
  public boolean isSpecial() {
    return true;
  }

  @Override
  public boolean showNotification() {
    return true;
  }

  @Override
  public String group() {
    return "";
  }

  @Override
  public List<RecipeDisplay> display() {
    return List.of(
        new ShapedCraftingRecipeDisplay(
            this.pattern.width(),
            this.pattern.height(),
            this.pattern.ingredients().stream()
                .map(ingredient -> ingredient
                    .map(Ingredient::display)
                    .orElse(SlotDisplay.Empty.INSTANCE))
                .toList(),
            new SlotDisplay.ItemStackSlotDisplay(this.result),
            new SlotDisplay.ItemSlotDisplay(RailcraftItems.MANUAL_ROLLING_MACHINE)
        )
    );
  }

  @Override
  public RecipeBookCategory recipeBookCategory() {
    return RecipeBookCategories.CRAFTING_MISC;
  }

  private static RollingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
    var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
    int processTime = buffer.readVarInt();
    var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
    return new RollingRecipe(pattern, result, processTime);
  }

  private static void toNetwork(RegistryFriendlyByteBuf buffer, RollingRecipe recipe) {
    ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
    buffer.writeVarInt(recipe.processTime);
    ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
  }
}
