/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.item;

import javax.annotation.Nullable;
import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

/**
 * Should be implemented by any rail item class that wishes to have it's rails placed by for example
 * the Tunnel Bore or Track Relayer.
 * <p/>
 * If you defined your rails with a TrackSpec, you don't need to worry about this.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
@ActivationBlockingItem
public interface TrackPlacer {

  /**
   * Attempts to place a track.
   *
   * @param stack The track to place
   * @param world The World object
   * @param pos The position
   * @param trackShape The preferred EnumRailDirection. May be null. If the shape is invalid for
   *        your track, use your default value.
   * @return true if successful
   */
  default boolean placeTrack(ItemStack stack, @Nullable PlayerEntity player, World world,
      BlockPos pos, @Nullable RailShape trackShape) {
    return stack.getItem().useOn(
        new ItemUseContext(player, Hand.MAIN_HAND, new BlockRayTraceResult(Vector3d.ZERO,
            Direction.UP, pos.below(), false))) == ActionResultType.SUCCESS;
  }

  /**
   * Return the block of a placed track.
   *
   * @return the blockId
   */
  Block getPlacedBlock();

  /**
   * Return true if the given tile entity corresponds to this Track item.
   * <p/>
   * If the track has no tile entity, return true on null.
   */
  boolean isPlacedTileEntity(ItemStack stack, @Nullable TileEntity tile);
}
