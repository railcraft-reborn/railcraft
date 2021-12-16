package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.api.core.NetworkSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface Module extends NetworkSerializable, INBTSerializable<CompoundTag> {

  ModuleProvider getProvider();

  void serverTick();
}
