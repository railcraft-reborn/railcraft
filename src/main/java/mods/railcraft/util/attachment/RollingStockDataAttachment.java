package mods.railcraft.util.attachment;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.api.carts.Train;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.world.entity.vehicle.LaunchState;
import mods.railcraft.world.entity.vehicle.TrainImpl;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class RollingStockDataAttachment implements INBTSerializable<CompoundTag> {

  @Nullable
  private TrainImpl train;

  @Nullable
  private RollingStock frontLink;
  @Nullable
  private RollingStock backLink;
  @Nullable
  private UUID unresolvedBackLink;
  @Nullable
  private UUID unresolvedFrontLink;

  private boolean backAutoLinkEnabled;
  private boolean frontAutoLinkEnabled;
  private LaunchState launchState = LaunchState.LANDED;
  private int elevatorRemainingTicks;
  private int preventMountRemainingTicks;
  private int derailedRemainingTicks;
  private boolean explosionPending;
  private boolean highSpeed;


  public boolean hasLink(Side side) {
    return switch (side) {
      case FRONT -> this.frontLink != null;
      case BACK -> this.backLink != null;
    };
  }

  @Nullable
  public RollingStock getLink(Side side) {
    return switch (side) {
      case FRONT -> this.frontLink;
      case BACK -> this.backLink;
    };
  }

  public Optional<RollingStock> linkAt(Function<UUID, Optional<RollingStock>> resolveLink,
      Side side) {
    this.resolveLinks(resolveLink);
    return Optional.ofNullable(getLink(side));
  }

  public void setLink(Side side, @Nullable RollingStock rollingStock) {
    switch (side) {
      case FRONT -> this.frontLink = rollingStock;
      case BACK -> this.backLink = rollingStock;
    }
  }

  private void resolveLinks(Function<UUID, Optional<RollingStock>> resolveLink) {
    if (this.unresolvedBackLink != null) {
      resolveLink.apply(this.unresolvedBackLink)
          .ifPresent(cart -> {
            this.unresolvedBackLink = null;
            this.backLink = cart;
          });
    }

    if (this.unresolvedFrontLink != null) {
      resolveLink.apply(this.unresolvedFrontLink)
          .ifPresent(cart -> {
            this.unresolvedFrontLink = null;
            this.frontLink = cart;
          });
    }
  }

  public Optional<Side> sideOf(RollingStock rollingStock) {
    var rollingStockUUID = rollingStock.entity().getUUID();
    if (rollingStockUUID.equals(this.unresolvedBackLink)) {
      this.unresolvedBackLink = null;
      this.backLink = rollingStock;
      return Optional.of(Side.BACK);
    }

    if (rollingStockUUID.equals(this.unresolvedFrontLink)) {
      this.unresolvedFrontLink = null;
      this.frontLink = rollingStock;
      return Optional.of(Side.FRONT);
    }

    if (this.backLink == rollingStock) {
      return Optional.of(Side.BACK);
    }

    if (this.frontLink == rollingStock) {
      return Optional.of(Side.FRONT);
    }

    return Optional.empty();
  }

  public boolean isFront(Function<UUID, Optional<RollingStock>> resolveLink) {
    this.resolveLinks(resolveLink);
    return this.frontLink == null;
  }

  public boolean isAutoLinkEnabled(Side side) {
    return switch (side) {
      case BACK -> this.backAutoLinkEnabled;
      case FRONT -> this.frontAutoLinkEnabled;
    };
  }

  public boolean setAutoLinkEnabled(Side side, boolean enabled) {
    switch (side) {
      case BACK -> this.backAutoLinkEnabled = enabled;
      case FRONT -> this.frontAutoLinkEnabled = enabled;
    }
    return true;
  }

  public boolean checkLaunchState(LaunchState state) {
    return this.launchState == state;
  }

  public void setLaunchState(LaunchState state) {
    this.launchState = state;
  }

  public int getElevatorRemainingTicks() {
    return this.elevatorRemainingTicks;
  }

  public void setElevatorRemainingTicks(int elevatorRemainingTicks) {
    this.elevatorRemainingTicks = elevatorRemainingTicks;
  }

  public void decrementElevatorRemainingTicks() {
    if (this.elevatorRemainingTicks > 0) {
      this.elevatorRemainingTicks--;
    }
  }

  public boolean isMountable() {
    return this.preventMountRemainingTicks <= 0;
  }

  public void setPreventMountRemainingTicks(int preventMountRemainingTicks) {
    this.preventMountRemainingTicks = preventMountRemainingTicks;
  }

  public void decrementPreventMountRemainingTicks() {
    this.preventMountRemainingTicks--;
  }

  public boolean isDerailed() {
    return this.derailedRemainingTicks > 0;
  }

  public void setDerailedRemainingTicks(int derailedRemainingTicks) {
    this.derailedRemainingTicks = derailedRemainingTicks;
  }

  public void setExplosionPending(boolean explosionPending) {
    this.explosionPending = explosionPending;
  }

  public boolean getExplosionPending() {
    return this.explosionPending;
  }

  public boolean isHighSpeed() {
    return this.highSpeed;
  }

  public void setHighSpeed(boolean highSpeed) {
    this.highSpeed = highSpeed;
  }

  public void decrementDerailedRemainingTicks() {
    if (this.derailedRemainingTicks > 0) {
      this.derailedRemainingTicks--;
    }
  }

  public boolean validateTrainOwnership(Function<UUID, Optional<RollingStock>> resolveLink,
      UUID idMinecartFront) {
    var front = this.isFront(resolveLink);
    if (!front && this.train != null) {
      this.train = null;
    }
    if (front && this.train == null) {
      this.train = TrainImpl.create(idMinecartFront);
    }
    return front;
  }

  public Train train(Function<UUID, Optional<RollingStock>> resolveLink, UUID idMinecartFront) {
    return this.validateTrainOwnership(resolveLink, idMinecartFront)
        ? this.train
        : this.getLink(Side.FRONT).train();
  }

  public void trainRefreshMaxSpeed(Level level) {
    this.train.refreshMaxSpeed(level);
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();

    if (this.train != null) {
      tag.put(CompoundTagKeys.TRAIN, this.train.toTag());
    }

    if (this.unresolvedBackLink != null) {
      tag.putUUID(CompoundTagKeys.BACK_LINK, this.unresolvedBackLink);
    } else if (this.backLink != null) {
      tag.putUUID(CompoundTagKeys.BACK_LINK, this.backLink.entity().getUUID());
    }

    if (this.unresolvedFrontLink != null) {
      tag.putUUID(CompoundTagKeys.FRONT_LINK, this.unresolvedFrontLink);
    } else if (this.frontLink != null) {
      tag.putUUID(CompoundTagKeys.FRONT_LINK, this.frontLink.entity().getUUID());
    }

    tag.putBoolean(CompoundTagKeys.BACK_AUTO_LINK_ENABLED, this.backAutoLinkEnabled);
    tag.putBoolean(CompoundTagKeys.FRONT_AUTO_LINK_ENABLED, this.frontAutoLinkEnabled);

    tag.putString(CompoundTagKeys.LAUNCH_STATE, this.launchState.getName());
    tag.putInt(CompoundTagKeys.ELEVATOR_REMAINING_TICKS, this.elevatorRemainingTicks);
    tag.putInt(CompoundTagKeys.PREVENT_MOUNT_REMAINING_TICKS, this.preventMountRemainingTicks);
    tag.putInt(CompoundTagKeys.DERAILED_REMAINING_TICKS, this.derailedRemainingTicks);
    tag.putBoolean(CompoundTagKeys.EXPLOSION_PENDING, this.explosionPending);
    tag.putBoolean(CompoundTagKeys.HIGH_SPEED, this.highSpeed);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.train = tag.contains(CompoundTagKeys.TRAIN, Tag.TAG_COMPOUND)
        ? TrainImpl.fromTag(tag.getCompound(CompoundTagKeys.TRAIN))
        : null;

    this.unresolvedBackLink = tag.hasUUID(CompoundTagKeys.BACK_LINK)
        ? tag.getUUID(CompoundTagKeys.BACK_LINK)
        : null;
    this.unresolvedFrontLink = tag.hasUUID(CompoundTagKeys.FRONT_LINK)
        ? tag.getUUID(CompoundTagKeys.FRONT_LINK)
        : null;

    this.backAutoLinkEnabled = tag.getBoolean(CompoundTagKeys.BACK_AUTO_LINK_ENABLED);
    this.frontAutoLinkEnabled = tag.getBoolean(CompoundTagKeys.FRONT_AUTO_LINK_ENABLED);

    this.launchState = LaunchState.fromName(tag.getString(CompoundTagKeys.LAUNCH_STATE));
    this.elevatorRemainingTicks = tag.getInt(CompoundTagKeys.ELEVATOR_REMAINING_TICKS);
    this.preventMountRemainingTicks = tag.getInt(CompoundTagKeys.PREVENT_MOUNT_REMAINING_TICKS);
    this.derailedRemainingTicks = tag.getInt(CompoundTagKeys.DERAILED_REMAINING_TICKS);
    this.explosionPending = tag.getBoolean(CompoundTagKeys.EXPLOSION_PENDING);
    this.highSpeed = tag.getBoolean(CompoundTagKeys.HIGH_SPEED);
  }
}
