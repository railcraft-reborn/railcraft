/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.track;

import net.minecraft.block.BlockState;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Any rail tile entity that can completely halt all cart movement should implement this interface.
 * (Used in collision handling)
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface LockdownTrack {

  boolean isCartLockedDown(BlockState blockState, World level, BlockPos pos,
      AbstractMinecartEntity cart);

  void releaseCart(BlockState blockState, World level, BlockPos pos);
}
