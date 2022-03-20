package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.api.core.NetworkSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

public interface Module extends NetworkSerializable, INBTSerializable<CompoundTag> {

  ModuleProvider getProvider();

  void serverTick();

  @Override
  default void writeToBuf(FriendlyByteBuf out) {}

  @Override
  default void readFromBuf(FriendlyByteBuf in) {}

  @Override
  default CompoundTag serializeNBT() {
    return new CompoundTag();
  }

  @Override
  default void deserializeNBT(CompoundTag tag) {}
}
