/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.core;

import java.util.Locale;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

/**
 * If you want your block to connect (or not connect) to posts, implement this interface.
 * <p/>
 * The result takes priority over any other rules.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IPostConnection {

  enum ConnectStyle implements IStringSerializable {

    NONE,
    SINGLE_THICK,
    TWO_THIN;

    @Override
    public String getSerializedName() {
      return name().toLowerCase(Locale.ROOT);
    }
  }

  /**
   * Return the ConnectStyle that should be used if the block at this location connects to a post.
   *
   * @param world The World
   * @param pos Our position
   * @param state Our BlockState
   * @param side Side to connect to
   * @return true if connect
   */
  ConnectStyle connectsToPost(IBlockReader world, BlockPos pos, BlockState state, Direction side);
}
