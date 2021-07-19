/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * Used by rails that modify the bounding boxes.
 *
 * For example, the Gated Rails.
 *
 * Not very useful since there is no system in place to insert custom render code.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface ITrackKitCustomShape extends ITrackKitInstance {

  VoxelShape getCollisionShape(BlockState state);

  VoxelShape getShape();
}
