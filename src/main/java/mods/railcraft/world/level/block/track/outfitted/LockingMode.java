package mods.railcraft.world.level.block.track.outfitted;

import java.util.function.Function;
import mods.railcraft.Translations;
import mods.railcraft.api.util.EnumUtil;
import mods.railcraft.world.level.block.entity.track.BoardingLockingProfile;
import mods.railcraft.world.level.block.entity.track.EmptyLockingProfile;
import mods.railcraft.world.level.block.entity.track.HoldingLockingProfile;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;

public enum LockingMode implements StringRepresentable {

  LOCKDOWN("lockdown", LockType.CART),
  TRAIN_LOCKDOWN("train_lockdown", LockType.TRAIN),
  HOLDING("holding", LockType.CART, HoldingLockingProfile::new),
  TRAIN_HOLDING("train_holding", LockType.TRAIN, HoldingLockingProfile::new),
  BOARDING("boarding", LockType.CART, BoardingLockingProfile::normal),
  BOARDING_REVERSED("boarding_reversed", LockType.CART, BoardingLockingProfile::reversed),
  TRAIN_BOARDING("train_boarding", LockType.TRAIN, BoardingLockingProfile::normal),
  TRAIN_BOARDING_REVERSED("train_boarding_reversed", LockType.TRAIN,
      BoardingLockingProfile::reversed);

  private final String name;
  private final LockType lockType;
  private final Function<? super LockingTrackBlockEntity, ? extends LockingModeController> factory;

  LockingMode(String name, LockType lockType) {
    this(name, lockType, __ -> EmptyLockingProfile.INSTANCE);
  }

  LockingMode(String name, LockType lockType,
      Function<? super LockingTrackBlockEntity, ? extends LockingModeController> factory) {
    this.name = name;
    this.lockType = lockType;
    this.factory = factory;
  }

  public Component getDisplayName() {
    return Component.translatable(this.getTranslationKey());
  }

  public String getTranslationKey() {
    return Translations.makeKey("tips", "locking_track." + this.name);
  }

  public LockType getLockType() {
    return this.lockType;
  }

  public LockingModeController create(LockingTrackBlockEntity lockingTrack) {
    return this.factory.apply(lockingTrack);
  }

  public LockingMode next() {
    return EnumUtil.next(this, values());
  }

  public LockingMode previous() {
    return EnumUtil.previous(this, values());
  }

  @Override
  public String getSerializedName() {
    return this.name;
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
