package mods.railcraft.world.level.material.fluid.tank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.client.util.RenderUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Our fancy type of tank. Use this preferably over forge's default one
 *
 * @see net.minecraftforge.fluids.capability.templates.FluidTank Forge FluidTank
 * @author CovertJaguar (https://www.railcraft.info)
 */
public class StandardTank extends FluidTank {

  public static final int DEFAULT_COLOR = 0xFFFFFF;

  private int tankIndex;
  private boolean hidden;

  @Nullable
  protected Supplier<FluidStack> filter;

  @Nullable
  private Runnable changeListener;

  private boolean disableDrain;
  private boolean disableFill;

  private List<Component> tooltip;

  public StandardTank(int capacity) {
    super(capacity);
  }

  @Override
  public StandardTank setValidator(Predicate<FluidStack> validator) {
    super.setValidator(validator);
    return this;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    return this.disableFill ? 0 : super.fill(resource, action);
  }


  @Override
  public FluidStack drain(FluidStack resource, FluidAction action) {
    return this.disableDrain ? FluidStack.EMPTY : super.drain(resource, action);
  }

  @Override
  public FluidStack drain(int maxDrain, FluidAction action) {
    return this.disableDrain ? FluidStack.EMPTY : super.drain(maxDrain, action);
  }

  /**
   * Internal fill function which IGNORES disablefill made by us.
   *
   * @param resource FluidStack representing the Fluid and maximum amount of fluid to be
   * @param action If SIMULATE, fill will only be simulated.
   * @return Amount of resource that was (or would have been, if simulated) filled.
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#fill(FluidStack, FluidAction) Forge FluidTank#fill()
   */
  public int internalFill(FluidStack resource, FluidAction action) {
    return super.fill(resource, action);
  }

  /**
   * Internal drain function which IGNORES disableDrain made by us.
   *
   * @param resource FluidStack representing the Fluid and maximum amount of fluid to be drained.
   * @param action If SIMULATE, fill will only be simulated.
   * @return FluidStack representing the Fluid and amount that was (or would have been, if
   *         simulated) drained.
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#drain(FluidStack, FluidAction)  Forge FluidTank#drain()
   */
  public FluidStack internalDrain(FluidStack resource, FluidAction action) {
    return super.drain(resource, action);
  }

  /**
   * Internal drain function which IGNORES disablefill made by us.
   *
   * @param maxDrain Maximum amount of fluid to drain.
   * @param action If SIMULATE, fill will only be simulated.
   * @return FluidStack representing the Fluid and amount that was (or would have been, if
   *         simulated) drained.
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#drain(int, FluidAction) () Forge FluidTank#drain()
   */
  public FluidStack internalDrain(int maxDrain, FluidAction action) {
    return super.drain(maxDrain, action);
  }

  /**
   * Disables draning of our tank. Blocks drain() from draining.
   *
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#drain(FluidStack, FluidAction)  Drain Function
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#internalDrain(FluidStack, FluidAction)  Bypassed Drain
   *      Function
   */
  public StandardTank disableDrain() {
    this.disableDrain = true;
    return this;
  }

  /**
   * Disables filling of our tank.
   *
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#fill(FluidStack, FluidAction)  Fill Function
   * @see mods.railcraft.world.level.material.fluid.tank.StandardTank#internalFill(FluidStack, FluidAction)  Bypassed Fill
   *      Function
   */
  public StandardTank disableFill() {
    this.disableFill = true;
    return this;
  }

  /**
   * Sets the tank's visibility.
   */
  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public StandardTank setChangeListener(@Nullable Runnable changeListener) {
    this.changeListener = changeListener;
    return this;
  }

  public int getTankIndex() {
    return tankIndex;
  }

  public void setTankIndex(int index) {
    this.tankIndex = index;
  }

  public int getColor() {
    Fluid f = getFluidType();
    if (f == null) {
      return DEFAULT_COLOR;
    }
    return RenderUtil.getColorARGB(getFluid());
  }

  public boolean isFull() {
    return getFluid().getAmount() == getCapacity();
  }

  public int getRemainingSpace() {
    return getCapacity() - getFluidAmount();
  }

  /**
   * Get the fluid type we currently have.
   *
   * @return Fluid type or <code>Fluids.EMPTY</code> if empty
   */
  public Fluid getFluidType() {
    return getFluid().getFluid();
  }

  @Override
  public void setFluid(@Nullable FluidStack resource) {
    if (!isFluidValid(resource)) {
      return;
    }
    super.setFluid(resource);
    this.onContentsChanged();
  }

  @Override
  protected void onContentsChanged() {
    this.refreshTooltip();
    if (this.changeListener != null) {
      this.changeListener.run();
    }
  }

  public List<Component> getTooltip() {
    return this.tooltip;
  }

  protected void refreshTooltip() {
    List<Component> tooltip = new ArrayList<>();
    int amount = getFluidAmount();
    FluidStack fluidStack = getFluid();

    if (fluidStack.isEmpty() && this.filter != null) {
      fluidStack = this.filter.get();
    }

    if (!fluidStack.isEmpty()) {
      tooltip.add(this.getFluidNameToolTip(fluidStack));
    }

    tooltip.add(Component.literal(String.format(Locale.ENGLISH, "%,d / %,d", amount, getCapacity()))
        .withStyle(ChatFormatting.GRAY));

    this.tooltip = Collections.unmodifiableList(tooltip);
  }

  protected Component getFluidNameToolTip(FluidStack fluidStack) {
    var fluidType = fluidStack.getFluid().getFluidType();
    var rarity = fluidType.getRarity();
    return fluidStack.getDisplayName().copy().withStyle(rarity.getStyleModifier());
  }

  public boolean isHidden() {
    return hidden;
  }
}
