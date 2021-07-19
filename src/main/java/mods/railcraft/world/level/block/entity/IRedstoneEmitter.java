package mods.railcraft.world.level.block.entity;

import net.minecraft.util.Direction;

public interface IRedstoneEmitter {

  int getSignal(Direction direction);
}
