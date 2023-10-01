package mods.railcraft.world.level.block.entity.detector;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;

public class PlayerDetectorBlockEntity extends EntityDetectorBlockEntity<Player> {

  public PlayerDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.PLAYER_DETECTOR.get(), blockPos, blockState, Player.class);
  }
}
