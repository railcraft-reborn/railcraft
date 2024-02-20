package mods.railcraft.world.level.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Our fancy type of tank. Use this preferably over forge's default one
 *
 * @see net.minecraftforge.fluids.capability.templates.FluidTank Forge FluidTank
 */
public class StandardTank extends FluidTank {

  @Nullable
  protected Supplier<FluidStack> filter;

  @Nullable
  private Runnable changeCallback;

  private boolean disableDrain;
  private boolean disableFill;

  private List<Component> tooltip;

  @Nullable
  private BiFunction<FluidStack, FluidAction, FluidStack> fillProcessor;

  private StandardTank(int capacity) {
    super(capacity);
    this.refreshTooltip();
  }

  public StandardTank fillProcessor(Function<FluidStack, FluidStack> fillProcessor) {
    return this.fillProcessor((resource, action) -> action.execute()
        ? fillProcessor.apply(resource)
        : resource);
  }

  public StandardTank fillProcessor(BiFunction<FluidStack, FluidAction, FluidStack> fillProcessor) {
    this.fillProcessor = fillProcessor;
    return this;
  }

  @SuppressWarnings("deprecation")
  public StandardTank filter(TagKey<Fluid> tag) {
    return this.setValidator(fluidStack -> fluidStack.getFluid().is(tag));
  }

  public StandardTank filter(Fluid filter) {
    return this.filter(() -> filter);
  }

  public StandardTank filter(Supplier<? extends Fluid> filter) {
    this.filter = () -> new FluidStack(filter.get(), 1);
    return this.setValidator(fluidStack -> this.filter.get().isFluidEqual(fluidStack));
  }

  @Override
  public StandardTank setValidator(Predicate<FluidStack> validator) {
    super.setValidator(validator);
    return this;
  }

  @Override
  public int fill(FluidStack resource, FluidAction action) {
    if (this.fillProcessor != null) {
      resource = this.fillProcessor.apply(resource, action);
    }
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
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#fill(FluidStack, FluidAction)
   *      Forge FluidTank#fill()
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
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#drain(FluidStack, FluidAction)
   *      Forge FluidTank#drain()
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
   * @see net.minecraftforge.fluids.capability.templates.FluidTank#drain(int, FluidAction) () Forge
   *      FluidTank#drain()
   */
  public FluidStack internalDrain(int maxDrain, FluidAction action) {
    return super.drain(maxDrain, action);
  }

  /**
   * Disables draning of our tank. Blocks drain() from draining.
   *
   * @see StandardTank#drain(FluidStack, FluidAction)
   *      Drain Function
   * @see StandardTank#internalDrain(FluidStack,
   *      FluidAction) Bypassed Drain Function
   */
  public StandardTank disableDrain() {
    this.disableDrain = true;
    return this;
  }

  /**
   * Disables filling of our tank.
   *
   * @see StandardTank#fill(FluidStack, FluidAction) Fill
   *      Function
   * @see StandardTank#internalFill(FluidStack,
   *      FluidAction) Bypassed Fill Function
   */
  public StandardTank disableFill() {
    this.disableFill = true;
    return this;
  }

  public StandardTank changeCallback(@Nullable Runnable changeCallback) {
    this.changeCallback = changeCallback;
    return this;
  }

  public boolean isFull() {
    return this.getFluid().getAmount() == this.getCapacity();
  }

  public int getRemainingSpace() {
    return this.getCapacity() - this.getFluidAmount();
  }

  /**
   * Get the fluid type we currently have.
   *
   * @return Fluid type or <code>Fluids.EMPTY</code> if empty
   */
  public Fluid getFluidType() {
    return this.getFluid().getFluid();
  }

  @Override
  public void setFluid(FluidStack resource) {
    if (resource.isEmpty() || this.isFluidValid(resource)) {
      super.setFluid(resource);
      this.onContentsChanged();
    }
  }

  @Override
  protected void onContentsChanged() {
    this.refreshTooltip();
    if (this.changeCallback != null) {
      this.changeCallback.run();
    }
  }

  public List<Component> getTooltip() {
    return this.tooltip;
  }

  protected void refreshTooltip() {
    var tooltip = new ArrayList<Component>();
    int amount = this.getFluidAmount();
    FluidStack fluidStack = this.getFluid();

    if (fluidStack.isEmpty() && this.filter != null) {
      fluidStack = this.filter.get();
    }

    if (fluidStack.isEmpty()) {
      tooltip.add(Component.translatable(Translations.Tips.EMPTY));
    } else {
      tooltip.add(this.getFluidNameToolTip(fluidStack));
    }

    tooltip.add(Component.literal(
        String.format(Locale.ENGLISH, "%,d / %,d", amount, this.getCapacity()))
        .withStyle(ChatFormatting.GRAY));

    this.tooltip = Collections.unmodifiableList(tooltip);
  }

  protected Component getFluidNameToolTip(FluidStack fluidStack) {
    var fluidType = fluidStack.getFluid().getFluidType();
    var rarity = fluidType.getRarity();
    return fluidStack.getDisplayName().copy().withStyle(rarity.getStyleModifier());
  }

  public static StandardTank ofBuckets(int buckets) {
    return ofCapacity(buckets * FluidType.BUCKET_VOLUME);
  }

  public static StandardTank ofCapacity(int capacity) {
    return new StandardTank(capacity);
  }
}
