package mods.railcraft.world.inventory.slots;

import java.util.List;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class RailcraftSlot extends Slot {

  @Nullable
  protected List<Component> tooltip;
  protected boolean phantom;
  protected boolean canAdjustPhantom = true;
  protected boolean canShift = true;
  protected int stackLimit = -1;
  public BooleanSupplier enabled = () -> true;

  public RailcraftSlot(Container container, int index, int x, int y) {
    super(container, index, x, y);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return this.enabled.getAsBoolean() && this.container.canPlaceItem(this.getSlotIndex(), stack);
  }

  public RailcraftSlot setEnableCheck(BooleanSupplier enabled) {
    this.enabled = enabled;
    return this;
  }

  /**
   * @return the toolTips
   */
  @Nullable
  public List<Component> getTooltip() {
    return tooltip;
  }

  /**
   * @param tooltip the tooltips to set
   */
  public void setTooltip(@Nullable List<Component> tooltip) {
    this.tooltip = tooltip;
  }

  public RailcraftSlot setPhantom() {
    this.phantom = true;
    return this;
  }

  public RailcraftSlot blockShift() {
    this.canShift = false;
    return this;
  }

  public RailcraftSlot setCanAdjustPhantom(boolean canAdjust) {
    this.canAdjustPhantom = canAdjust;
    return this;
  }

  public RailcraftSlot setCanShift(boolean canShift) {
    this.canShift = canShift;
    return this;
  }

  public RailcraftSlot setStackLimit(int limit) {
    this.stackLimit = limit;
    return this;
  }

  @Override
  public final int getMaxStackSize() {
    int max = super.getMaxStackSize();
    return this.stackLimit < 0 ? max : Math.min(max, this.stackLimit); // issue #1347
  }

  public boolean isPhantom() {
    return this.phantom;
  }

  public boolean canAdjustPhantom() {
    return this.canAdjustPhantom;
  }

  @Override
  public boolean mayPickup(Player player) {
    return !this.isPhantom();
  }

  public boolean canShift() {
    return this.canShift;
  }
}
