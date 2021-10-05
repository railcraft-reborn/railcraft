package mods.railcraft.world.level.block.track.behaivor;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.util.TrackTools;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 8/7/2016 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public class TrackSupportTools {

  public static boolean isSupportedDirectly(IBlockReader level, BlockPos pos) {
    return Block.canSupportRigidBlock(level, pos.below());
  }

  public static boolean isSupported(World level, BlockPos pos) {
    return isSupported(level, pos, 2);
  }

  public static boolean isSupported(IWorldReader level, BlockPos pos, int maxDistance) {
    return maxDistance == 0
        ? isSupportedDirectly(level, pos)
        : isSupported(level, pos, false, maxDistance, new HashSet<>());
  }

  @SuppressWarnings("deprecation")
  private static boolean isSupported(IWorldReader level, BlockPos pos, boolean checkSelf,
      int distance,
      Set<BlockPos> checked) {
    if (checked.contains(pos))
      return false;
    checked.add(pos);
    if (!level.hasChunkAt(pos))
      return true;
    if (checkSelf && !AbstractRailBlock.isRail(level.getBlockState(pos)))
      return false;
    if (isSupportedDirectly(level, pos))
      return true;
    if (distance <= 0)
      return false;
    distance--;
    for (BlockPos connectedTrack : TrackTools.getConnectedTracks(level, pos)) {
      if (isSupported(level, connectedTrack, true, distance, checked))
        return true;
    }
    return false;
  }
}
