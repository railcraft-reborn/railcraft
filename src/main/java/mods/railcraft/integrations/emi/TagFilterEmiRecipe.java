package mods.railcraft.integrations.emi;

import java.util.List;
import java.util.Random;
import dev.emi.emi.api.recipe.EmiPatternCraftingRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import dev.emi.emi.api.widget.GeneratedSlotWidget;
import dev.emi.emi.api.widget.SlotWidget;
import dev.emi.emi.api.widget.WidgetHolder;
import mods.railcraft.Translations;
import mods.railcraft.integrations.jei.RailcraftJeiPlugin;
import mods.railcraft.world.item.RailcraftItems;
import mods.railcraft.world.item.crafting.TagFilterRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TagFilterEmiRecipe extends EmiPatternCraftingRecipe {

  private static final EmiStack FILTER = EmiStack.of(RailcraftItems.TAG_FILTER.get());
  private final List<ItemStack> prototypes = TagFilterRecipe.getExamplePrototypes();
  private final List<ItemStack> results = TagFilterRecipe.getExampleResults();

  public TagFilterEmiRecipe(ResourceLocation id) {
    super(List.of(FILTER, EmiIngredient.of(TagFilterRecipe.getExamplePrototypes().stream()
        .map(EmiStack::of)
        .toList())), FILTER, id, true);
  }

  @Override
  public SlotWidget getInputWidget(int slot, int x, int y) {
    return switch (slot) {
      case 0 -> new SlotWidget(FILTER, x, y);
      case 1 -> new GeneratedSlotWidget(random -> EmiStack.of(this.prototypes.get(index(random))),
          this.unique, x, y);
      default -> new SlotWidget(EmiStack.EMPTY, x, y);
    };
  }

  @Override
  public SlotWidget getOutputWidget(int x, int y) {
    return new GeneratedSlotWidget(random -> EmiStack.of(this.results.get(index(random))),
        this.unique, x, y);
  }

  /**
   * The input and output widgets are seeded alike, so they pick the same example.
   */
  private int index(Random random) {
    return random.nextInt(this.prototypes.size());
  }

  @Override
  public void addWidgets(WidgetHolder widgets) {
    super.addWidgets(widgets);
    var info = Component.translatable(Translations.Jei.SET_PROTOTYPE);
    var font = Minecraft.getInstance().font;
    widgets.addText(info, 82 - font.width(info) / 2, 0, RailcraftJeiPlugin.TEXT_COLOR, false);
  }
}
