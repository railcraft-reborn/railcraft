package mods.railcraft.world.level.material;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import org.jspecify.annotations.Nullable;
import mods.railcraft.Translations;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidUtil;
import net.neoforged.neoforge.transfer.resource.Resource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;

/**
 * Our fancy type of tank. Use this preferably over neoforge's default one
 *
 * @see net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler FluidStacksResourceHandler
 */
public class StandardTank extends FluidStacksResourceHandler {

  @Nullable
  protected Supplier<FluidStack> filter;
  private Predicate<FluidResource> validator;

  @Nullable
  private Runnable changeCallback;
  @Nullable
  private Consumer<FluidStack> fillProcessor;

  private boolean disableExtract;
  private boolean disableInsert;

  private List<Component> tooltip;

  private final FluidStackJournal fluidStackJournal = new FluidStackJournal();

  private StandardTank(int capacity) {
    super(1, capacity);
    this.refreshTooltip();
    this.validator = __ -> true;
  }

  /**
   * Used to trigger explosion when inserting water into a superheated tank
   */
  public StandardTank fillProcessor(Consumer<FluidStack> fillProcessor) {
    this.fillProcessor = fillProcessor;
    return this;
  }

  public StandardTank filter(TagKey<Fluid> tag) {
    return this.setValidator(fluidResource -> fluidResource.is(tag));
  }

  public StandardTank filter(Fluid filter) {
    return this.filter(() -> filter);
  }

  public StandardTank filter(Supplier<? extends Fluid> filter) {
    this.filter = () -> new FluidStack(filter.get(), 1);
    return this.setValidator(fluidResource -> fluidResource.is(filter.get()));
  }

  public void setCapacity(int capacity) {
    this.capacity = capacity;
  }

  public FluidStack getFluidStack() {
    return FluidUtil.getStack(this, 0);
  }

  public int getCapacity() {
    return this.getCapacity(0, FluidResource.EMPTY);
  }

  public int getFluidAmount() {
    return this.getAmountAsInt(0);
  }

  public boolean isEmpty() {
    return this.getFluidAmount() <= 0;
  }

  public boolean isFull() {
    return this.getFluidAmount() == this.getCapacityAsInt(0, FluidResource.EMPTY);
  }

  public int getRemainingSpace() {
    return this.getCapacityAsInt(0, FluidResource.EMPTY) - this.getFluidAmount();
  }

  public FluidType getFluidType() {
    return this.getFluidStack().getFluidType();
  }

  public void setFluid(FluidStack fluidStack) {
    this.set(0, FluidResource.of(fluidStack), fluidStack.getAmount());
  }

  public StandardTank setValidator(Predicate<FluidResource> validator) {
    this.validator = validator;
    return this;
  }

  @Override
  public boolean isValid(int index, FluidResource resource) {
    return this.validator.test(resource);
  }

  @Override
  public int insert(FluidResource resource, int amount, TransactionContext transaction) {
    fluidStackJournal.updateSnapshots(transaction);
    return this.disableInsert ? 0 : super.insert(resource, amount, transaction);
  }

  /**
   * Internal fill function which IGNORES <code>disablefill</code> made by us.
   *
   * @see net.neoforged.neoforge.transfer.ResourceHandler#insert(Resource, int, TransactionContext)
   */
  public int internalInsert(FluidResource resource, int amount, TransactionContext transaction) {
    return super.insert(resource, amount, transaction);
  }

  @Override
  public int extract(FluidResource resource, int amount, TransactionContext transaction) {
    return this.disableExtract ? 0 : super.extract(resource, amount, transaction);
  }

  /**
   * Internal drain function which IGNORES <code>disableDrain</code> made by us.
   *
   * @see net.neoforged.neoforge.transfer.ResourceHandler#extract(Resource, int, TransactionContext)
   */
  public int internalExtract(FluidResource resource, int amount, TransactionContext transaction) {
    return super.extract(resource, amount, transaction);
  }

  /**
   * Disables draining of our tank.
   *
   * @see StandardTank#extract(FluidResource, int, TransactionContext) Extract Function
   * @see StandardTank#internalExtract(FluidResource, int, TransactionContext) Bypassed extract function
   */
  public StandardTank disableExtract() {
    this.disableExtract = true;
    return this;
  }

  /**
   * Disables filling of our tank.
   *
   * @see StandardTank#insert(FluidResource, int, TransactionContext) Insert Function
   * @see StandardTank#internalInsert(FluidResource, int, TransactionContext) Bypassed insert Function
   */
  public StandardTank disableInsert() {
    this.disableInsert = true;
    return this;
  }

  public StandardTank changeCallback(@Nullable Runnable changeCallback) {
    this.changeCallback = changeCallback;
    return this;
  }

  @Override
  public void set(int index, FluidResource resource, int amount) {
    if (resource.isEmpty() || this.isValid(index, resource)) {
      super.set(index, resource, amount);
    }
  }

  @Override
  protected void onContentsChanged(int index, FluidStack previousContents) {
    this.refreshTooltip();
    if (this.changeCallback != null) {
      this.changeCallback.run();
    }
  }

  private class FluidStackJournal extends SnapshotJournal<FluidStack> {

    @Override
    protected FluidStack createSnapshot() {
      return getFluidStack();
    }

    @Override
    protected void revertToSnapshot(FluidStack snapshot) {
      set(0, FluidResource.of(snapshot), snapshot.getAmount());
    }

    @Override
    protected void onRootCommit(FluidStack originalState) {
      if (fillProcessor != null) {
        fillProcessor.accept(originalState);
      }
    }
  }

  public List<Component> getTooltip() {
    return this.tooltip;
  }

  protected void refreshTooltip() {
    var tooltip = new ArrayList<Component>();
    int amount = this.getFluidAmount();
    FluidStack fluidStack = this.getFluidStack();

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
    var fluidType = fluidStack.getFluidType();
    var rarity = fluidType.getRarity();
    return fluidStack.getHoverName().copy().withStyle(rarity.getStyleModifier());
  }

  public static StandardTank ofBuckets(int buckets) {
    return ofCapacity(buckets * FluidType.BUCKET_VOLUME);
  }

  public static StandardTank ofCapacity(int capacity) {
    return new StandardTank(capacity);
  }
}
