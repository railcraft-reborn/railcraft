package mods.railcraft.world.level.block.track.outfitted;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import mods.railcraft.world.level.block.entity.track.BoardingLockingProfile;
import mods.railcraft.world.level.block.entity.track.EmptyLockingProfile;
import mods.railcraft.world.level.block.entity.track.HoldingLockingProfile;
import mods.railcraft.world.level.block.entity.track.LockingTrackBlockEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public enum LockingMode implements IStringSerializable {

  LOCKDOWN("lockdown", LockType.CART),
  TRAIN_LOCKDOWN("train_lockdown", LockType.TRAIN),
  HOLDING("holding", LockType.CART, HoldingLockingProfile::new),
  TRAIN_HOLDING("train_holding", LockType.TRAIN, HoldingLockingProfile::new),
  BOARDING("boarding", LockType.CART, BoardingLockingProfile::normal),
  BOARDING_REVERSED("boarding_reversed", LockType.CART, BoardingLockingProfile::reversed),
  TRAIN_BOARDING("train_boarding", LockType.TRAIN, BoardingLockingProfile::normal),
  TRAIN_BOARDING_REVERSED("train_boarding_reversed", LockType.TRAIN,
      BoardingLockingProfile::reversed);

  private static final Map<String, LockingMode> byName = Arrays.stream(values())
      .collect(Collectors.toMap(LockingMode::getSerializedName, Function.identity()));

  private final String name;
  private final ITextComponent displayName;
  private final LockType lockType;
  private final Function<? super LockingTrackBlockEntity, ? extends LockingModeController> factory;

  private LockingMode(String name, LockType lockType) {
    this(name, lockType, __ -> EmptyLockingProfile.INSTANCE);
  }

  private LockingMode(String name, LockType lockType,
      Function<? super LockingTrackBlockEntity, ? extends LockingModeController> factory) {
    this.name = name;
    this.displayName = new TranslationTextComponent("locking_track.mode." + name);
    this.lockType = lockType;
    this.factory = factory;
  }

  public ITextComponent getDisplayName() {
    return this.displayName;
  }

  public LockType getLockType() {
    return this.lockType;
  }

  public LockingModeController create(LockingTrackBlockEntity lockingTrack) {
    return this.factory.apply(lockingTrack);
  }

  public LockingMode next() {
    return values()[(ordinal() + 1) % values().length];
  }

  public LockingMode previous() {
    return values()[(ordinal() + values().length - 1) % values().length];
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  public static Optional<LockingMode> getByName(String name) {
    return Optional.ofNullable(byName.get(name));
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
