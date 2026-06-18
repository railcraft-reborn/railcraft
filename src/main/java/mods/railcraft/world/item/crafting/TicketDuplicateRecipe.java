package mods.railcraft.world.item.crafting;

import java.util.stream.IntStream;
import org.jspecify.annotations.Nullable;
import com.mojang.serialization.MapCodec;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TicketDuplicateRecipe extends CustomRecipe {

  private static final TicketDuplicateRecipe INSTANCE = new TicketDuplicateRecipe();
  private static final MapCodec<TicketDuplicateRecipe> MAP_CODEC = MapCodec.unit(INSTANCE);
  private static final StreamCodec<RegistryFriendlyByteBuf, TicketDuplicateRecipe> STREAM_CODEC = StreamCodec.unit(INSTANCE);
  public static final RecipeSerializer<TicketDuplicateRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

  private static final Ingredient SOURCE = Ingredient.of(RailcraftItems.GOLDEN_TICKET.get());
  private static final Ingredient BLANK = Ingredient.of(Items.PAPER);
  @Nullable
  private PlacementInfo placementInfo;

  @Override
  public boolean matches(CraftingInput craftingInput, Level level) {
    if (craftingInput.width() * craftingInput.height() < 2) {
      return false;
    }

    int numBlank = 0;
    int numSource = 0;
    for (int slot = 0; slot < craftingInput.size(); slot++) {
      var stack = craftingInput.getItem(slot);
      if (!stack.isEmpty()) {
        if (numSource == 0 && SOURCE.test(stack)) {
          numSource++;
        } else if (BLANK.test(stack)) {
          numBlank++;
        } else {
          return false;
        }
      }
    }
    return numSource == 1 && numBlank == 1;
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput) {
    var source = IntStream.range(0, craftingInput.size())
        .mapToObj(craftingInput::getItem)
        .filter(TicketDuplicateRecipe.SOURCE)
        .findFirst()
        .orElse(ItemStack.EMPTY);
    var result = new ItemStack(RailcraftItems.TICKET.get());
    if (!source.isEmpty()) {
      if (source.has(RailcraftDataComponents.TICKET)) {
        result.set(RailcraftDataComponents.TICKET, source.get(RailcraftDataComponents.TICKET));
      }
    }
    return result;
  }

  @Override
  public PlacementInfo placementInfo() {
    if (this.placementInfo == null) {
      NonNullList<Ingredient> ingredients = NonNullList.create();
      ingredients.add(Ingredient.of(RailcraftItems.GOLDEN_TICKET.get()));
      ingredients.add(Ingredient.of(Items.PAPER));
      this.placementInfo = PlacementInfo.create(ingredients);
    }
    return this.placementInfo;
  }

  @Override
  public RecipeSerializer<TicketDuplicateRecipe> getSerializer() {
    return SERIALIZER;
  }
}
