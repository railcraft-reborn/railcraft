package mods.railcraft.world.level.block.entity.track;

import mods.railcraft.world.level.block.track.outfitted.LockingModeController;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;

public enum EmptyLockingProfile implements LockingModeController {

  INSTANCE;

  @Override
  public CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  @Override
  public void deserializeNBT(CompoundNBT tag) {}

  @Override
  public void locked(AbstractMinecartEntity cart) {}

  @Override
  public void passed(AbstractMinecartEntity cart) {}

  @Override
  public void released(AbstractMinecartEntity cart) {}
}
