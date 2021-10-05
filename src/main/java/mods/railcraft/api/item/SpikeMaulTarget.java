/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import java.util.ArrayList;
import java.util.List;
import mods.railcraft.api.track.TrackType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 3/6/2017 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public interface SpikeMaulTarget {

  /**
   * A list for registering or changing spike maul targets.
   */
  List<SpikeMaulTarget> spikeMaulTargets = new ArrayList<>();

  /**
   * Returns true when the given state is your resulting state.
   *
   * @param world The world
   * @param pos The position
   * @param state The block state
   * @return True if the given state is the target's result
   */
  boolean matches(World world, BlockPos pos, BlockState state);

  /**
   * Returns true when you successfully set another state to your resulting state. Return false to
   * revert changes.
   *
   * @param world The world
   * @param pos The position
   * @param state The block state
   * @param player The player
   * @param shape The rail direction
   * @param trackType The track type
   * @return If operation is successful
   */
  boolean setToTarget(World world, BlockPos pos, BlockState state, PlayerEntity player,
      RailShape shape, TrackType trackType);
}
