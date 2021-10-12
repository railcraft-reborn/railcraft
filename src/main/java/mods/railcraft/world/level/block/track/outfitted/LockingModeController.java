package mods.railcraft.world.level.block.track.outfitted;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

public interface LockingModeController extends INBTSerializable<CompoundNBT> {

  default void locked(AbstractMinecartEntity cart) {}

  default void passed(AbstractMinecartEntity cart) {}

  default void released(AbstractMinecartEntity cart) {}

  default CompoundNBT serializeNBT() {
    return new CompoundNBT();
  }

  default void deserializeNBT(CompoundNBT tag) {}
}
