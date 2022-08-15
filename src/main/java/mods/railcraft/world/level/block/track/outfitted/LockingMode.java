package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Function;
import mods.railcraft.Translations.Tips;
import mods.railcraft.world.level.block.entity.track.BoardingLockingProfile;
import mods.railcraft.world.level.block.entity.track.EmptyLockingProfile;
import mods.railcraft.world.level.block.entity.track.HoldingLockingProfile;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum LockingMode implements StringRepresentable {

  LOCKDOWN(LockType.CART),
  TRAIN_LOCKDOWN(LockType.TRAIN),
  HOLDING(LockType.CART, HoldingLockingProfile::new),
  TRAIN_HOLDING(LockType.TRAIN, HoldingLockingProfile::new),
  BOARDING(LockType.CART, BoardingLockingProfile::normal),
  BOARDING_REVERSED(LockType.CART, BoardingLockingProfile::reversed),
  TRAIN_BOARDING(LockType.TRAIN, BoardingLockingProfile::normal),
  TRAIN_BOARDING_REVERSED(LockType.TRAIN, BoardingLockingProfile::reversed);

  private final LockType lockType;
  private final Function<? super LockingTrackBlockEntity, ? extends LockingModeController> factory;

  LockingMode(LockType lockType) {
    this(lockType, __ -> EmptyLockingProfile.INSTANCE);
  }

  LockingMode(LockType lockType,
      Function<? super LockingTrackBlockEntity, ? extends LockingModeController> factory) {
    this.lockType = lockType;
    this.factory = factory;
  }

  public Component getDisplayName() {
    return Component.translatable(getTip());
  }

  private String getTip() {
    return switch (this.ordinal()) {
      case 0 -> Tips.LOCKING_TRACK_LOCKDOWN;
      case 1 -> Tips.LOCKING_TRACK_TRAIN_LOCKDOWN;
      case 2 -> Tips.LOCKING_TRACK_HOLDING;
      case 3 -> Tips.LOCKING_TRACK_TRAIN_HOLDING;
      case 4 -> Tips.LOCKING_TRACK_BOARDING;
      case 5 -> Tips.LOCKING_TRACK_BOARDING_REVERSED;
      case 6 -> Tips.LOCKING_TRACK_TRAIN_BOARDING;
      case 7 -> Tips.LOCKING_TRACK_TRAIN_BOARDING_REVERSED;
      default -> "translation.not.implemented";
    };
  }

  public LockType getLockType() {
    return this.lockType;
  }

  public LockingModeController create(LockingTrackBlockEntity lockingTrack) {
    return this.factory.apply(lockingTrack);
  }

  public LockingMode next() {
    return values()[(this.ordinal() + 1) % values().length];
  }

  public LockingMode previous() {
    return values()[(this.ordinal() + values().length - 1) % values().length];
  }

  @Override
  public String getSerializedName() {
    var name = getTip().split("\\.");
    return name[name.length - 1];
  }

  public enum LockType {

    CART, TRAIN;

    public boolean isCart() {
      return this == CART;
    }

    public boolean isTrain() {
      return this == TRAIN;
    }
  }
}
