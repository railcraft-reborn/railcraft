package mods.railcraft.world.inventory;

import java.util.List;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.FormattedText;

/**
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class SlotRailcraft extends Slot {

  @Nullable
  protected List<? extends FormattedText> toolTips;
  protected boolean isPhantom;
  protected boolean canAdjustPhantom = true;
  protected boolean canShift = true;
  protected int stackLimit = -1;
  public BooleanSupplier isEnabled = () -> true;

  public SlotRailcraft(Container iinventory, int slotIndex, int posX, int posY) {
    super(iinventory, slotIndex, posX, posY);
  }

  @Override
  public boolean mayPlace(ItemStack stack) {
    return isEnabled.getAsBoolean() && this.container.canPlaceItem(getSlotIndex(), stack);
  }

  public SlotRailcraft setEnableCheck(BooleanSupplier isEnabled) {
    this.isEnabled = isEnabled;
    return this;
  }

  /**
   * @return the toolTips
   */
  @Nullable
  public List<? extends FormattedText> getTooltip() {
    return toolTips;
  }

  /**
   * @param toolTips the tooltips to set
   */
  public void setTooltip(@Nullable List<? extends FormattedText> toolTips) {
    this.toolTips = toolTips;
  }

  public SlotRailcraft setPhantom() {
    isPhantom = true;
    return this;
  }

  public SlotRailcraft blockShift() {
    canShift = false;
    return this;
  }

  public SlotRailcraft setCanAdjustPhantom(boolean canAdjust) {
    this.canAdjustPhantom = canAdjust;
    return this;
  }

  public SlotRailcraft setCanShift(boolean canShift) {
    this.canShift = canShift;
    return this;
  }

  public SlotRailcraft setStackLimit(int limit) {
    this.stackLimit = limit;
    return this;
  }

  @Override
  public final int getMaxStackSize() {
    int max = super.getMaxStackSize();
    return stackLimit < 0 ? max : Math.min(max, stackLimit); // issue #1347
  }

  public boolean isPhantom() {
    return isPhantom;
  }

  public boolean canAdjustPhantom() {
    return canAdjustPhantom;
  }

  @Override
  public boolean mayPickup(Player stack) {
    return !isPhantom();
  }

  public boolean canShift() {
    return canShift;
  }
}
