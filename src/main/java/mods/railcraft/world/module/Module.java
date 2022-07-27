package mods.railcraft.world.module;

import mods.railcraft.api.core.NetworkSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.common.util.INBTSerializable;

/**
 * Similar to an entity component system, the module system is intended for modularisation of logic
 * for reuse, organisation and compositional purposes. It is preferred to put logic into a module
 * rather than using inheritance as that is limited by the fact you can only extend a single class.
 * 
 * @author Sm0keySa1m0n
 */
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
