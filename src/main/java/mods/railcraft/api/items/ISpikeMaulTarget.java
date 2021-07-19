/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.items;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackToolsAPI;
import mods.railcraft.api.tracks.TrackType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Created by CovertJaguar on 3/6/2017 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ISpikeMaulTarget {
  /**
   * A list for registering or changing spike maul targets.
   */
  List<ISpikeMaulTarget> spikeMaulTargets = new ArrayList<>();

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

  class TrackKitTarget implements ISpikeMaulTarget {
    private final Supplier<TrackKit> trackKit;

    public TrackKitTarget(Supplier<TrackKit> trackKit) {
      this.trackKit = trackKit;
    }

    @Override
    public boolean matches(World world, BlockPos pos, BlockState state) {
      return TrackToolsAPI.getTrackKit(world, pos) == trackKit.get();
    }

    @Override
    public boolean setToTarget(World world,
        BlockPos pos,
        BlockState state,
        PlayerEntity player,
        RailShape shape,
        TrackType trackType) {

      // TODO this
      return false;
      // return TrackToolsAPI.blockTrackOutfitted.place(world, pos, player, shape, trackType,
      // trackKit.get());
    }
  }
}
