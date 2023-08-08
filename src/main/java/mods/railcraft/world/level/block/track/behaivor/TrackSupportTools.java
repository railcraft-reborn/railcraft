package mods.railcraft.world.level.block.track.behaivor;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.track.TrackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;

public class TrackSupportTools {

  public static boolean isSupportedDirectly(BlockGetter level, BlockPos pos) {
    return Block.canSupportRigidBlock(level, pos.below());
  }

  public static boolean isSupported(Level level, BlockPos pos) {
    return isSupported(level, pos, 2);
  }

  public static boolean isSupported(LevelReader level, BlockPos pos, int maxDistance) {
    return maxDistance == 0
        ? isSupportedDirectly(level, pos)
        : isSupported(level, pos, false, maxDistance, new HashSet<>());
  }

  @SuppressWarnings("deprecation")
  private static boolean isSupported(LevelReader level, BlockPos pos, boolean checkSelf,
      int distance, Set<BlockPos> checked) {
    if (checked.contains(pos)) {
      return false;
    }
    checked.add(pos);
    if (!level.hasChunkAt(pos)) {
      return true;
    }
    if (checkSelf && !BaseRailBlock.isRail(level.getBlockState(pos))) {
      return false;
    }
    if (isSupportedDirectly(level, pos)) {
      return true;
    }
    if (distance <= 0) {
      return false;
    }
    distance--;
    for (var connectedTrack : TrackUtil.getConnectedTracks(level, pos)) {
      if (isSupported(level, connectedTrack, true, distance, checked)) {
        return true;
      }
    }
    return false;
  }
}
