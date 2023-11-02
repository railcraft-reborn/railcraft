package mods.railcraft.world.item.crafting;

import java.util.stream.IntStream;
import mods.railcraft.world.item.RailcraftItems;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class TicketDuplicateRecipe extends CustomRecipe {

  private static final Ingredient SOURCE = Ingredient.of(RailcraftItems.GOLDEN_TICKET.get());
  private static final Ingredient BLANK = Ingredient.of(Items.PAPER);

  public TicketDuplicateRecipe(CraftingBookCategory category) {
    super(category);
  }

  @Override
  public boolean matches(CraftingContainer container, Level level) {
    int numBlank = 0;
    int numSource = 0;
    for (int slot = 0; slot < container.getContainerSize(); slot++) {
      var stack = container.getItem(slot);
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
  public ItemStack assemble(CraftingContainer container, RegistryAccess registryAccess) {
    var source = IntStream.range(0, container.getContainerSize())
        .mapToObj(container::getItem)
        .filter(TicketDuplicateRecipe.SOURCE)
        .findFirst()
        .orElse(ItemStack.EMPTY);
    var result = getResultItem(registryAccess);
    if (!source.isEmpty()) {
      var nbt = source.getTag();
      if (nbt != null)
        result.setTag(nbt.copy());
    }
    return result;
  }

  @Override
  public NonNullList<Ingredient> getIngredients() {
    NonNullList<Ingredient> ingredients = NonNullList.create();
    ingredients.add(Ingredient.of(RailcraftItems.GOLDEN_TICKET.get()));
    ingredients.add(Ingredient.of(Items.PAPER));
    return ingredients;
  }

  @Override
  public ItemStack getResultItem(RegistryAccess registryAccess) {
    return new ItemStack(RailcraftItems.TICKET.get());
  }

  @Override
  public boolean canCraftInDimensions(int width, int height) {
    return width * height >= 2;
  }

  @Override
  public RecipeSerializer<?> getSerializer() {
    return RailcraftRecipeSerializers.TICKET_DUPLICATE.get();
  }
}
