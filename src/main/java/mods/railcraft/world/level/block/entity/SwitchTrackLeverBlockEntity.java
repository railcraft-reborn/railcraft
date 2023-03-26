package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.track.SwitchActuator;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.state.BlockState;

public class SwitchTrackLeverBlockEntity extends RailcraftBlockEntity implements SwitchActuator {

  public SwitchTrackLeverBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_LEVER.get(), blockPos, blockState);
  }

  @Override
  public boolean shouldSwitch(@Nullable AbstractMinecart cart) {
    return SwitchTrackActuatorBlock.isSwitched(this.getBlockState());
  }
}
