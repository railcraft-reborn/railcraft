package mods.railcraft.api.carts;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import com.mojang.authlib.GameProfile;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.SlotAccessor;
import mods.railcraft.api.track.TrackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.capabilities.AutoRegisterCapability;
import net.neoforged.neoforge.common.capabilities.Capabilities;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityManager;
import net.neoforged.neoforge.common.capabilities.CapabilityToken;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;

/**
 * Main capability for all things minecart/locomotive related.
 * 
 * @author Sm0keySa1m0n
 */
@AutoRegisterCapability
public interface RollingStock {

  /**
   * The default max distance between linked rolling stock, divided by 2.
   */
  float MAX_LINK_DISTANCE = 1.25F;
  /**
   * The default distance between linked rolling stock, divided by 2.
   */
  float OPTIMAL_LINK_DISTANCE = 0.78F;
  /**
   * The fastest speed rolling stock can go before they are considered high speed.
   */
  float HIGH_SPEED_THRESHOLD = 0.499F;
  /**
   * The fastest speed rolling stock can go before they explode.
   */
  float EXPLOSION_SPEED_THRESHOLD = 0.5F;

  int MAX_BLOCKING_ITEM_SLOTS = 8;
  int MAX_BLOCKING_TANK_CAPACITY = 8 * FluidType.BUCKET_VOLUME;

  Capability<RollingStock> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  static RollingStock getOrThrow(AbstractMinecart minecart) {
    return minecart.getCapability(CAPABILITY)
        .orElseThrow(() -> new IllegalStateException("RollingStock missing on " + minecart));
  }

  /**
   * Called upon minecart tick.
   * 
   * @apiNote Called by Railcraft.
   */
  @ApiStatus.Internal
  void tick();

  /**
   * Called upon minecart removal.
   * 
   * @param reason - the reason
   * @apiNote Called by Railcraft.
   */
  @ApiStatus.Internal
  void removed(Entity.RemovalReason reason);

  boolean hasLink(Side side);

  default boolean isLinked() {
    return this.hasLink(Side.FRONT) || this.hasLink(Side.BACK);
  }

  /**
   * @return if this {@link RollingStock} is at the front of the train.
   */
  default boolean isFront() {
    return !this.hasLink(Side.FRONT);
  }

  /**
   * @return if this {@link RollingStock} is at the back of the train.
   */
  default boolean isBack() {
    return !this.hasLink(Side.BACK);
  }

  /**
   * @return if this {@link RollingStock} is at the front or back of the train.
   */
  default boolean isEnd() {
    return this.isFront() || this.isBack();
  }

  /**
   * Retrieves the {@link RollingStock} linked to the specified {@link Side}.
   * 
   * @param side - the {@link Side} to retrieve
   * @return the linked {@link RollingStock} or {@link Optional#empty()} if no link exists.
   */
  Optional<RollingStock> linkAt(Side side);

  /**
   * Shorthand for {@link #linkAt(Side.FRONT)}
   */
  default Optional<RollingStock> frontLink() {
    return this.linkAt(Side.FRONT);
  }

  /**
   * Shorthand for {@link #linkAt(Side.BACK)}
   */
  default Optional<RollingStock> backLink() {
    return this.linkAt(Side.BACK);
  }

  /**
   * Retrieves the {@link Side} on which the specified {@link RollingStock} is linked.
   * 
   * @param rollingStock - the {@link RollingStock} to check for
   * @return the {@link Side} on which it's linked or {@link Optional#empty()} if no link exists.
   */
  Optional<Side> sideOf(RollingStock rollingStock);

  default boolean isLinkedWith(RollingStock minecart) {
    return this.sideOf(minecart).isPresent();
  }

  default Optional<Side> disabledSide() {
    return this.entity() instanceof Linkable handler
        ? handler.disabledSide()
        : Optional.empty();
  }

  /**
   * Link with the specified {@link RollingStock}.
   * 
   * @param rollingStock - the {@code RollingStock} to link
   * @return {@code true} if linking was successful, {@code false} otherwise
   */
  boolean link(RollingStock rollingStock);

  /**
   * Called when a link is completed.
   * 
   * @param rollingStock - the {@code RollingStock} to link
   * @apiNote To be called by implementations of {@link RollingStock}
   */
  @ApiStatus.Internal
  void completeLink(RollingStock rollingStock, Side side);

  /**
   * Determine whether the specified {@link RollingStock} can be linked with.
   * 
   * @param rollingStock - the {@link RollingStock} being linked
   * @return {@code true} if it can, {@code false} otherwise
   * @apiNote To be called by implementations of {@link RollingStock}
   */
  @ApiStatus.Internal
  default boolean isLinkableWith(RollingStock rollingStock) {
    if (this.disabledSide().map(Side::opposite).map(this::hasLink).orElse(false)) {
      return false;
    }
    if (this.entity() instanceof Linkable handler
        && !handler.isLinkableWith(rollingStock)) {
      return false;
    }
    return true;
  }

  /**
   * Called when a link is removed.
   * 
   * @param side - the link side to be removed
   * @apiNote To be called by implementations of {@link RollingStock}
   */
  @ApiStatus.Internal
  void removeLink(Side side);

  default boolean unlinkAll() {
    return Stream.of(Side.values()).allMatch(this::unlink);
  }

  default boolean unlink(RollingStock minecart) {
    return this.sideOf(minecart).map(this::unlink).orElse(false);
  }

  boolean unlink(Side Link);

  /**
   * Traverses all {@link RollingStock} on the specified {@link Side} and swaps their front and back
   * links.
   * 
   * @apiNote To be called by implementations of {@link RollingStock}.
   */
  @ApiStatus.Internal
  boolean swapLinks(Side side);

  default Stream<RollingStock> traverseTrain(Side side) {
    Spliterator<RollingStock> spliterator =
        new Spliterators.AbstractSpliterator<>(Long.MAX_VALUE,
            Spliterator.ORDERED | Spliterator.IMMUTABLE) {

          private RollingStock current = RollingStock.this;

          @Override
          public boolean tryAdvance(Consumer<? super RollingStock> action) {
            Objects.requireNonNull(action);

            if (this.current == null) {
              return false;
            }

            this.current = this.current.linkAt(side).orElse(null);
            if (this.current == null) {
              return false;
            }

            action.accept(this.current);

            return true;
          }
        };
    return StreamSupport.stream(spliterator, false);
  }

  default Stream<RollingStock> traverseTrainWithSelf(Side side) {
    return Stream.concat(Stream.of(this), this.traverseTrain(side));
  }

  Train train();

  default boolean isSameTrainAs(@NotNull RollingStock rollingStock) {
    Objects.requireNonNull(rollingStock, "rollingStock cannot be null.");
    return this.train() == rollingStock.train();
  }

  /**
   * Pushes an {@link ItemStack} through a train.
   *
   * @param itemStack - the {@link ItemStack} to be pushed
   * @return the remaining {@link ItemStack}, or {@link ItemStack#EMPTY} if all items were pushed
   * @see {@link ItemTransferHandler}
   */
  default ItemStack pushItem(ItemStack itemStack) {
    for (var side : Side.values()) {
      Iterable<RollingStock> targets =
          () -> this.traverseTrain(side).iterator();
      for (var target : targets) {
        var cart = target.entity();
        var adaptor = cart.getCapability(Capabilities.ITEM_HANDLER)
            .map(ContainerManipulator::of)
            .orElse(null);
        if (adaptor != null && this.canAcceptPushedItem(cart, itemStack)) {
          itemStack = adaptor.insert(itemStack);
        }

        if (itemStack.isEmpty() || blocksItemRequests(cart, itemStack)) {
          break;
        }
      }

      if (itemStack.isEmpty()) {
        return ItemStack.EMPTY;
      }
    }

    return itemStack;
  }

  private boolean canAcceptPushedItem(AbstractMinecart cart, ItemStack stack) {
    return !(cart instanceof ItemTransferHandler handler)
        || handler.canAcceptPushedItem(this, stack);
  }

  /**
   * Pulls an {@link ItemStack} from a train.
   *
   * @param filter - a {@link Predicate} to filter the pulled item
   * @return the resulting {@link ItemStack} or {@link ItemStack#EMPTY} if not found
   * @see {@link ItemTransferHandler}
   */
  default ItemStack pullItem(Predicate<ItemStack> filter) {
    for (var side : Side.values()) {
      Iterable<RollingStock> targets = () -> this.traverseTrain(side).iterator();
      RollingStock resultProvider = null;
      SlotAccessor result = null;
      for (var target : targets) {
        var cart = target.entity();
        var slot = cart.getCapability(Capabilities.ITEM_HANDLER)
            .map(ContainerManipulator::of)
            .flatMap(manipulator -> manipulator.findFirstExtractable(
                filter.and(stack -> this.canProvidePulledItem(cart, stack))))
            .orElse(null);
        if (slot != null) {
          resultProvider = target;
          result = slot;
        }
      }

      if (result == null) {
        continue;
      }

      for (var target : targets) {
        if (target == resultProvider) {
          break;
        }
        if (blocksItemRequests(target.entity(), result.item())) {
          result = null;
          break;
        }
      }

      if (result != null) {
        return result.extract();
      }
    }

    return ItemStack.EMPTY;
  }

  private boolean canProvidePulledItem(AbstractMinecart cart, ItemStack stack) {
    return !(cart instanceof ItemTransferHandler handler)
        || handler.canProvidePulledItem(this, stack);
  }

  /**
   * Pushes an {@link ItemStack} through the train and drops any remaining items.
   *
   * @param itemStack - the {@link ItemStack} to be pushed
   */
  default void offerOrDropItem(ItemStack itemStack) {
    var remainder = this.pushItem(itemStack);
    if (!remainder.isEmpty()) {
      Containers.dropItemStack(this.level(),
          this.entity().getX(), this.entity().getY(), this.entity().getZ(),
          remainder);
    }
  }

  /**
   * Pushes the specified {@link FluidStack} through the train.
   *
   * @param fluidStack - the {@link FluidStack} to be pushed
   * @return the remaining {@link FluidStack}, or {@link FluidStack#EMPTY} if all fluid was pushed
   * @see {@link FluidTransferHandler}
   */
  default FluidStack pushFluid(FluidStack fluidStack) {
    var remainder = fluidStack.copy();
    for (var side : Side.values()) {
      Iterable<RollingStock> targets = () -> this.traverseTrain(side).iterator();
      for (var target : targets) {
        var cart = target.entity();
        if (this.canAcceptPushedFluid(cart, remainder)) {
          var fluidHandler = cart.getCapability(Capabilities.FLUID_HANDLER).orElse(null);
          if (fluidHandler != null) {
            var filled = fluidHandler.fill(remainder, IFluidHandler.FluidAction.EXECUTE);
            remainder.setAmount(remainder.getAmount() - filled);
          }
        }
        if (remainder.isEmpty() || blocksFluidRequests(cart, remainder)) {
          break;
        }
      }

      if (remainder.isEmpty()) {
        return FluidStack.EMPTY;
      }
    }

    return remainder;
  }

  private boolean canAcceptPushedFluid(AbstractMinecart cart, FluidStack fluid) {
    return !(cart instanceof FluidTransferHandler handler)
        || handler.canAcceptPushedFluid(this, fluid);
  }

  /**
   * Pulls a {@link FluidStack} from a train.
   *
   * @param fluidStack - the {@link FluidStack} to pull
   * @return the resulting {@link FluidStack} or {@link FluidStack#EMPTY} if not found
   * @see {@link FluidTransferHandler}
   */
  default FluidStack pullFluid(FluidStack fluidStack) {
    if (fluidStack.isEmpty()) {
      return FluidStack.EMPTY;
    }

    for (var side : Side.values()) {
      Iterable<RollingStock> targets = () -> this.traverseTrain(side).iterator();
      for (var target : targets) {
        var cart = target.entity();
        if (this.canProvidePulledFluid(cart, fluidStack)) {
          var fluidHandler = cart.getCapability(Capabilities.FLUID_HANDLER).orElse(null);
          if (fluidHandler != null) {
            var drained = fluidHandler.drain(fluidStack, IFluidHandler.FluidAction.EXECUTE);
            if (!drained.isEmpty()) {
              return drained;
            }
          }
        }

        if (blocksFluidRequests(cart, fluidStack)) {
          break;
        }
      }
    }

    return FluidStack.EMPTY;
  }

  private boolean canProvidePulledFluid(AbstractMinecart cart, FluidStack fluid) {
    return !(cart instanceof FluidTransferHandler handler)
        || handler.canProvidePulledFluid(this, fluid);
  }

  default boolean isAutoLinkEnabled() {
    return Stream.of(Side.values()).anyMatch(this::isAutoLinkEnabled);
  }

  boolean isAutoLinkEnabled(Side link);

  boolean setAutoLinkEnabled(Side link, boolean enabled);

  default boolean setAutoLinkEnabled(boolean enabled) {
    var result = false;
    for (var link : Side.values()) {
      result |= this.setAutoLinkEnabled(link, enabled);
    }
    return result;
  }

  default boolean tryAutoLink(RollingStock cart2) {
    return (this.isAutoLinkEnabled() || cart2.isAutoLinkEnabled()) && this.link(cart2);
  }

  default boolean canCartBeAdjustedBy(RollingStock cart2) {
    if (this == cart2) {
      return false;
    }
    if (this.entity() instanceof Linkable handler && !handler.canBeAdjusted(cart2)) {
      return false;
    }
    return !TrackUtil.isCartLocked(this.entity());
  }

  boolean isLaunched();

  void launch();

  int getElevatorRemainingTicks();

  default boolean isOnElevator() {
    return this.getElevatorRemainingTicks() > 0;
  }

  void setElevatorRemainingTicks(int elevatorRemainingTicks);

  boolean isMountable();

  void setPreventMountRemainingTicks(int preventMountRemainingTicks);

  boolean isDerailed();

  void setDerailedRemainingTicks(int derailedRemainingTicks);

  void primeExplosion();

  boolean isHighSpeed();

  void checkHighSpeed(BlockPos blockPos);

  Optional<GameProfile> owner();

  AbstractMinecart entity();

  default Level level() {
    return this.entity().level();
  }

  private static boolean blocksItemRequests(AbstractMinecart cart, ItemStack stack) {
    return cart instanceof ItemTransferHandler handler
        ? !handler.canPassItemRequests(stack)
        : cart.getCapability(Capabilities.ITEM_HANDLER)
            .map(IItemHandler::getSlots)
            .orElse(0) < MAX_BLOCKING_ITEM_SLOTS;
  }

  private static boolean blocksFluidRequests(AbstractMinecart cart, FluidStack fluid) {
    return cart instanceof FluidTransferHandler fluidMinecart
        ? !fluidMinecart.canPassFluidRequests(fluid)
        : cart.getCapability(Capabilities.FLUID_HANDLER)
            .map(fluidHandler -> !hasMatchingTank(fluidHandler, fluid))
            .orElse(true);
  }

  private static boolean hasMatchingTank(IFluidHandler handler, FluidStack fluid) {
    for (int i = 0; i < handler.getTanks(); i++) {
      if (handler.getTankCapacity(i) >= MAX_BLOCKING_TANK_CAPACITY) {
        var tankFluid = handler.getFluidInTank(i);
        if (tankFluid.isEmpty() || tankFluid.isFluidEqual(fluid)) {
          return true;
        }
      }
    }
    return false;
  }
}
