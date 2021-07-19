/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.tracks;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import mods.railcraft.api.core.INetworkedObject;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This interface defines a track.
 *
 * Basically all block and tile entity functions for Tracks are delegated to an ITrackInstance.
 *
 * Instead of implementing this interface directly, you should probably extend TrackInstanceBase. It
 * will simplify your life.
 *
 * You must have a constructor that accepts a single TileEntity object.
 *
 * All packet manipulation is handled by Railcraft's code, you just need to implement the functions
 * in INetworkedObject to pass data from the server to the client.
 *
 * @author CovertJaguar
 * @see TrackKitInstance
 */
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public interface ITrackKitInstance extends INetworkedObject {

  <T extends TileEntity & IOutfittedTrackTile> T getTile();

  <T extends TileEntity & IOutfittedTrackTile> void setTile(T tileEntity);

  TrackKit getTrackKit();

  /**
   * Return the render state. Ranges from 0 to 15. Used by the TrackKit blockstate JSON to determine
   * which model/texture to display.
   */
  default int getRenderState() {
    return 0;
  }

  default List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
    List<ItemStack> drops = new ArrayList<>();
    drops.add(getTrackKit().getTrackKitItem());
    return drops;
  }

  /**
   * Return the rail's shape. Can be used to make the cart think the rail something other than it
   * is, for example when making diamond junctions or switches.
   *
   * @param cart The cart asking for the metadata, null if it is not called by
   *        AbstractMinecartEntity.
   * @return The metadata.
   */
  RailShape getRailDirection(BlockState state, @Nullable AbstractMinecartEntity cart);

  /**
   * This function is called by any minecart that passes over this rail. It is called once per
   * update tick that the minecart is on the rail.
   *
   * @param cart The cart on the rail.
   */
  default void onMinecartPass(AbstractMinecartEntity cart) {}

  default void writeToNBT(CompoundNBT data) {}

  default void readFromNBT(CompoundNBT data) {}

  default void tick() {}

  ActionResultType use(PlayerEntity player, Hand hand);

  default void onBlockRemoved() {}

  void setPlacedBy(BlockState state, @Nullable LivingEntity placer, ItemStack stack);

  void neighborChanged(BlockState state, @Nullable Block neighborBlock);

  default BlockPos getPos() {
    return getTile().getBlockPos();
  }

  @Override
  default @Nullable World theWorld() {
    return getTile().getLevel();
  }

  /**
   * Returns the max speed of the rail.
   *
   * @param cart The cart on the rail, may be null.
   * @return The max speed of the current rail.
   */
  default double getRailMaxSpeed(World world, @Nullable AbstractMinecartEntity cart, BlockPos pos) {
    return getTrackType().getEventHandler().getMaxSpeed(world, cart, pos);
  }

  /**
   * Returning true here will make the track unbreakable.
   */
  default boolean isProtected() {
    return false;
  }

  /**
   * Returns the track type of this track.
   */
  default TrackType getTrackType() {
    return getTile().getTrackType();
  }

  /**
   * Requests for saving the data of the track kit.
   */
  default void markDirty() {
    getTile().setChanged();
  }
}
