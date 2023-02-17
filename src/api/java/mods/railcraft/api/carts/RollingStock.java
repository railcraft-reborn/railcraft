package mods.railcraft.api.carts;

import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import mods.railcraft.api.track.TrackUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

/**
 * Main capability for all things minecart/locomotive related.
 * 
 * @author Sm0keySa1m0n
 */
public interface RollingStock {

  /**
   * The default max distance at which carts can be linked, divided by 2.
   */
  float MAX_LINK_DISTANCE = 1.25F;
  /**
   * The default distance at which linked carts are maintained, divided by 2.
   */
  float OPTIMAL_LINK_DISTANCE = 0.78F;

  Capability<RollingStock> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});

  static RollingStock getOrThrow(AbstractMinecart minecart) {
    return minecart.getCapability(CAPABILITY)
        .orElseThrow(() -> new IllegalStateException("MinecartExtension missing on " + minecart));
  }

  /**
   * Called upon minecart tick.
   */
  @ApiStatus.Internal
  void tick();

  /**
   * Called upon minecart removal.
   * 
   * @param reason - the reason
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
   * Complete linking with {@link RollingStock}.
   * 
   * @param rollingStock - the {@code RollingStock} to link
   * @apiNote <b>Only to be called by implementations of {@link RollingStock}.</b>
   */
  @ApiStatus.Internal
  void completeLink(RollingStock rollingStock, Side side);

  /**
   * Determine whether the specified {@link RollingStock} can be linked with.
   * 
   * @param rollingStock - the {@link RollingStock} being linked
   * @return {@code true} if it can, {@code false} otherwise
   * @apiNote <b>Only to be called by implementations of {@link RollingStock}.</b>
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
   * @apiNote <b>Only to be called by implementations of {@link RollingStock}.</b>
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

  default boolean isSameTrainAs(@NotNull RollingStock minecart) {
    Objects.requireNonNull(minecart, "minecart cannot be null.");
    return this.train() == minecart.train();
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

  void setHighSpeed(boolean highSpeed);

  AbstractMinecart entity();

  default Level level() {
    return this.entity().getLevel();
  }
}
