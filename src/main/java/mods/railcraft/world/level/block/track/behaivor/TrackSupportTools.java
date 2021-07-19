package mods.railcraft.world.level.block.track.behaivor;

import java.util.HashSet;
import java.util.Set;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.util.TrackTools;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

/**
 * Created by CovertJaguar on 8/7/2016 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TrackSupportTools {

  public static boolean isSupportedDirectly(IBlockReader world, BlockPos pos) {
    return Block.canSupportRigidBlock(world, pos.below());
  }

  public static boolean isSupported(IWorldReader world, BlockPos pos) {
    return isSupported(world, pos, 2);
  }

  public static boolean isSupported(IWorldReader world, BlockPos pos, int maxDistance) {
    if (maxDistance == 0)
      return isSupportedDirectly(world, pos);
    return isSupported(world, pos, false, maxDistance, new HashSet<>());
  }

  @SuppressWarnings("deprecation")
  private static boolean isSupported(IWorldReader world, BlockPos pos, boolean checkSelf,
      int distance,
      Set<BlockPos> checked) {
    if (checked.contains(pos))
      return false;
    checked.add(pos);
    if (!world.hasChunkAt(pos))
      return true;
    if (checkSelf && !TrackToolsAPI.isRailBlockAt(world, pos))
      return false;
    if (isSupportedDirectly(world, pos))
      return true;
    if (distance <= 0)
      return false;
    distance--;
    for (BlockPos connectedTrack : TrackTools.getConnectedTracks(world, pos)) {
      if (isSupported(world, connectedTrack, true, distance, checked))
        return true;
    }
    return false;
  }
}
