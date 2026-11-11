package mods.railcraft.integrations.forestry;

/*
import java.util.function.Predicate;
import java.util.function.Supplier;
import com.google.common.base.Suppliers;
import forestry.api.IForestryApi;
import forestry.api.core.backpacks.IBackpackDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// A Forestry backpack whose contents are driven by an allow and a reject item tag.
//
// Only load this class when Forestry is installed.
public final class RailcraftBackpackDefinition implements IBackpackDefinition {

  private final int primaryColour;
  private final int secondaryColour;
  private final Supplier<Predicate<ItemStack>> filter;

  public RailcraftBackpackDefinition(int primaryColour, int secondaryColour,
      TagKey<Item> allowTag, TagKey<Item> rejectTag) {
    this.primaryColour = primaryColour;
    this.secondaryColour = secondaryColour;
    this.filter = Suppliers.memoize(() -> IForestryApi.INSTANCE.getBackpackInterface()
        .createBackpackFilter(allowTag, rejectTag));
  }

  @Override
  public Component getName(ItemStack backpack) {
    // Not backpack.getHoverName(): Forestry's BackpackItem#getName calls this method
    return Component.translatable(backpack.getItem().getDescriptionId());
  }

  @Override
  public int getPrimaryColour() {
    return this.primaryColour;
  }

  @Override
  public int getSecondaryColour() {
    return this.secondaryColour;
  }

  @Override
  public Predicate<ItemStack> getFilter() {
    return this.filter.get();
  }
}
*/
