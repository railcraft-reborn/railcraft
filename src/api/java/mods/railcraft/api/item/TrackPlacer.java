/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.item;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Should be implemented by any rail item class that wishes to have it's rails placed by for example
 * the Tunnel Bore or Track Relayer.
 * <p/>
 * If you defined your rails with a TrackSpec, you don't need to worry about this.
 */
@ActivationBlockingItem
public interface TrackPlacer {

  /**
   * Attempts to place a track.
   *
   * @param stack The track to place
   * @param level The World object
   * @param pos The position
   * @param trackShape The preferred EnumRailDirection. May be null. If the shape is invalid for
   *        your track, use your default value.
   * @return true if successful
   */
  default boolean placeTrack(ItemStack stack, @NotNull Player player, Level level,
      BlockPos pos, @Nullable RailShape trackShape) {
    return stack.getItem().useOn(
        new UseOnContext(player, InteractionHand.MAIN_HAND, new BlockHitResult(Vec3.ZERO,
            Direction.UP, pos.below(), false))) == InteractionResult.SUCCESS;
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
  boolean isPlacedTileEntity(ItemStack stack, @Nullable BlockEntity tile);
}
