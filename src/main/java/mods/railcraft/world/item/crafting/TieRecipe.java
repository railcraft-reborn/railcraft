package mods.railcraft.world.item.crafting;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.NonNullList;
import net.minecraft.tags.TagKey;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.access.ItemAccess;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;
import net.neoforged.neoforge.transfer.transaction.Transaction;

public abstract class TieRecipe extends CustomRecipe {

  private final TagKey<Fluid> fluidTag;
  /**
   * Lazy, as recipe instances are created while the serializers are registered, before item
   * components have been bound.
   */
  private final Supplier<ItemStack> result;
  @Nullable
  private PlacementInfo placementInfo;

  public TieRecipe(TagKey<Fluid> fluidTag, Supplier<ItemStack> result) {
    super();
    this.fluidTag = fluidTag;
    this.result = result;
  }

  @Override
  public final boolean matches(CraftingInput craftingInput, Level level) {
    if (craftingInput.width() != 3 || craftingInput.height() != 2) {
      return false;
    }

    if (!craftingInput.getItem(0).isEmpty() || !craftingInput.getItem(2).isEmpty()) {
      return false;
    }

    if (!FluidUtil.getFirstStackContained(craftingInput.getItem(1)).is(this.fluidTag)) {
      return false;
    }

    return testIngredient(craftingInput.getItem(3), 0) &&
        testIngredient(craftingInput.getItem(4), 1) &&
        testIngredient(craftingInput.getItem(5), 2);
  }

  protected abstract boolean testIngredient(ItemStack itemPresent, int index);

  /** What to show in the slot that takes the fluid container. */
  protected abstract SlotDisplay fluidContainerDisplay();

  /** What to show in the three slots of the bottom row. */
  protected abstract List<SlotDisplay> materialDisplays();

  @Override
  public List<RecipeDisplay> display() {
    var materials = this.materialDisplays();
    var slots = new ArrayList<SlotDisplay>(6);
    slots.add(SlotDisplay.Empty.INSTANCE);
    slots.add(this.fluidContainerDisplay());
    slots.add(SlotDisplay.Empty.INSTANCE);
    slots.addAll(materials);
    return List.of(new ShapedCraftingRecipeDisplay(3, 2, slots,
        new SlotDisplay.ItemStackSlotDisplay(
            ItemStackTemplate.fromNonEmptyStack(this.result.get())),
        new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)));
  }

  @Override
  public ItemStack assemble(CraftingInput craftingInput) {
    // Only produce a result if the container can actually give up the fluid, so that
    // getRemainingItems never has to hand back a container it failed to drain.
    return drain(craftingInput.getItem(1)) == null
        ? ItemStack.EMPTY
        : this.result.get().copy();
  }

  @Override
  public final NonNullList<ItemStack> getRemainingItems(CraftingInput input) {
    var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
    for (int i = 0; i < remainingItems.size(); ++i) {
      ItemStack item = input.getItem(i);
      if (item.isEmpty()) {
        continue;
      }
      if (item.getCraftingRemainder() != null) {
        remainingItems.set(i, item.getCraftingRemainder().create());
      } else {
        var drained = drain(item);
        if (drained != null) {
          remainingItems.set(i, drained);
        }
      }
    }
    return remainingItems;
  }

  /**
   * Removes one bucket of fluid from a single copy of the given container, leaving the container
   * passed in untouched.
   *
   * @return the emptied container, or {@code null} if a bucket could not be drained
   */
  @Nullable
  private static ItemStack drain(ItemStack container) {
    if (container.isEmpty()) {
      return null;
    }
    var slot = new SimpleContainer(container.copyWithCount(1));
    var cap = ItemAccess.forHandlerIndex(VanillaContainerWrapper.of(slot), 0)
        .getCapability(Capabilities.Fluid.ITEM);
    if (cap == null) {
      return null;
    }

    // Resolve the fluid before opening a transaction, so a failed extraction leaves nothing behind.
    for (int i = 0; i < cap.size(); i++) {
      var resource = cap.getResource(i);
      if (resource.isEmpty()) {
        continue;
      }
      try (var tx = Transaction.openRoot()) {
        if (cap.extract(resource, FluidType.BUCKET_VOLUME, tx) != FluidType.BUCKET_VOLUME) {
          return null;
        }
        tx.commit();
      }
      return slot.getItem(0);
    }
    return null;
  }
}
