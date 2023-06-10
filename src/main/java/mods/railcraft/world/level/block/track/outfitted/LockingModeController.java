package mods.railcraft.world.level.block.track.outfitted;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.common.util.INBTSerializable;

public interface LockingModeController extends INBTSerializable<CompoundTag> {

  default void locked(AbstractMinecart cart) {}

  default void passed(AbstractMinecart cart) {}

  default void released(AbstractMinecart cart) {}

  default CompoundTag serializeNBT() {
    return new CompoundTag();
  }

  default void deserializeNBT(CompoundTag tag) {}
}
