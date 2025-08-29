package mods.railcraft.world.level.block.track.outfitted;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public interface LockingModeController extends ValueIOSerializable {

  default void locked(AbstractMinecart cart) {}

  default void passed(AbstractMinecart cart) {}

  default void released(AbstractMinecart cart) {}

  @Override
  default void serialize(ValueOutput valueOutput) {}

  @Override
  default void deserialize(ValueInput valueInput) {}
}
