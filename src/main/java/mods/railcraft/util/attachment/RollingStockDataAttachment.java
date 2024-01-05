package mods.railcraft.util.attachment;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.world.entity.vehicle.LaunchState;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

public class RollingStockDataAttachment implements INBTSerializable<CompoundTag> {

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

  public void setLink(Side side, @Nullable RollingStock minecart) {
    switch (side) {
      case FRONT -> this.frontLink = minecart;
      case BACK -> this.backLink = minecart;
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

  @Override
  public CompoundTag serializeNBT() {
    var tag = new CompoundTag();
    if (this.unresolvedBackLink != null) {
      tag.putUUID("backLink", this.unresolvedBackLink);
    } else if (this.backLink != null) {
      tag.putUUID("backLink", this.backLink.entity().getUUID());
    }

    if (this.unresolvedFrontLink != null) {
      tag.putUUID("frontLink", this.unresolvedFrontLink);
    } else if (this.frontLink != null) {
      tag.putUUID("frontLink", this.frontLink.entity().getUUID());
    }

    tag.putBoolean("backAutoLinkEnabled", this.backAutoLinkEnabled);
    tag.putBoolean("frontAutoLinkEnabled", this.frontAutoLinkEnabled);

    tag.putString("launchState", this.launchState.getName());
    tag.putInt("elevatorRemainingTicks", this.elevatorRemainingTicks);
    tag.putInt("preventMountRemainingTicks", this.preventMountRemainingTicks);
    tag.putInt("derailedRemainingTicks", this.derailedRemainingTicks);
    tag.putBoolean("explosionPending", this.explosionPending);
    tag.putBoolean("highSpeed", this.highSpeed);
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.unresolvedBackLink = tag.hasUUID("backLink")
        ? tag.getUUID("backLink")
        : null;
    this.unresolvedFrontLink = tag.hasUUID("frontLink")
        ? tag.getUUID("frontLink")
        : null;

    this.backAutoLinkEnabled = tag.getBoolean("backAutoLinkEnabled");
    this.frontAutoLinkEnabled = tag.getBoolean("frontAutoLinkEnabled");

    this.launchState = LaunchState.getByName(tag.getString("launchState"))
        .orElse(LaunchState.LANDED);
    this.elevatorRemainingTicks = tag.getInt("elevatorRemainingTicks");
    this.preventMountRemainingTicks = tag.getInt("preventMountRemainingTicks");
    this.derailedRemainingTicks = tag.getInt("derailedRemainingTicks");
    this.explosionPending = tag.getBoolean("explosionPending");
    this.highSpeed = tag.getBoolean("highSpeed");
  }
}
