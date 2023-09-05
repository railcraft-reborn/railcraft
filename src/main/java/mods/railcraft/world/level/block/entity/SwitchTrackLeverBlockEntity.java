package mods.railcraft.world.level.block.entity;

import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.track.SwitchActuator;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchTrackLeverBlockEntity extends RailcraftBlockEntity implements SwitchActuator {

  public SwitchTrackLeverBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_LEVER.get(), blockPos, blockState);
  }

  @Override
  public boolean shouldSwitch(RollingStock cart) {
    return SwitchTrackActuatorBlock.isSwitched(this.getBlockState());
  }
}
