/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.tracks;

import net.minecraft.block.Block;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.world.World;
import javax.annotation.Nullable;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public class TrackKitMissing extends TrackKitInstance {

  private final boolean swapOut;
  private int counter;

  public TrackKitMissing() {
    this(true);
  }

  public TrackKitMissing(boolean swapOut) {
    this.swapOut = swapOut;
  }

  @Override
  public TrackKit getTrackKit() {
    return TrackRegistry.getMissingTrackKit();
  }

  @Override
  public void tick() {
    swapTrack();
  }

  @Override
  public void neighborChanged(BlockState state, @Nullable Block neighborBlock) {
    super.neighborChanged(state, neighborBlock);
    swapTrack();
  }

  private void swapTrack() {
    World world = theWorld();
    if (swapOut && world != null && !world.isClientSide() && counter > 4) {
      BlockState oldState = world.getBlockState(getPos());
      AbstractRailBlock oldBlock = (AbstractRailBlock) oldState.getBlock();
      TrackType type = getTile().getTrackType();
      AbstractRailBlock newBlock = type.getBaseBlock();
      @SuppressWarnings("deprecation")
      BlockState newState = newBlock.defaultBlockState().setValue(newBlock.getShapeProperty(),
          oldState.getValue(oldBlock.getShapeProperty()));
      world.setBlockAndUpdate(getPos(), newState);
    }
    counter++;
  }
}
