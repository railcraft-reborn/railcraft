/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Random;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

/**
 * This interface must be implement by any {@link net.minecraft.block.Block Block} that wants to
 * interface with any of the charge networks.
 *
 * <p>
 * Created by CovertJaguar on 7/25/2016 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public interface ChargeBlock {

  /**
   * Asks the Block to provide a map of ChargeSpecs for each network.
   *
   * <p>
   * It is generally to be considered an error to return the same charge definition to multiple
   * networks. Most blocks will probably be members of the {@link Charge#distribution} network only.
   *
   * <p>
   * Only "transformer" blocks that pass charge from one network to another should respond to
   * multiple networks.
   *
   * @return A mapping of networks to ChargeSpecs. Most blocks should only respond to one type of
   *         network or an empty map. If an empty map is returned, the charge networks will ignore
   *         the block.
   */
  default Map<Charge, ChargeBlock.Spec> getChargeSpecs(BlockState state, ServerLevel level,
      BlockPos pos) {
    return Collections.emptyMap();
  }

  /**
   * The Charge Meter calls this to get access for meter readings.
   *
   * <p>
   * Most blocks don't need to touch this, but Multi-blocks may want to redirect to the master
   * block.
   */
  @Nullable
  default Charge.Access getMeterAccess(Charge network, BlockState state, ServerLevel level,
      BlockPos pos) {
    return network.network(level).access(pos);
  }

  /**
   * Helper method for registering a block to the network.
   *
   * <p>
   * This function must be called from the following functions:
   * {@link net.minecraft.block.Block#onBlockAdded(World, BlockPos, BlockState)}
   * {@link net.minecraft.block.Block#updateTick(World, BlockPos, BlockState, Random)}
   *
   * <p>
   * The block must set {@link net.minecraft.block.Block#setTickRandomly(boolean)} to true in the
   * constructor.
   */
  default void registerNode(BlockState state, ServerLevel level, BlockPos pos) {
    EnumSet.allOf(Charge.class).forEach(n -> n.network(level).addNode(pos, state));
  }

  /**
   * Helper method for removing a block from the networks.
   *
   * <p>
   * This function must be called from the following function:
   * {@link BlockBehaviour#onRemove(BlockState, Level, BlockPos, BlockState, boolean)}.
   */
  default void deregisterNode(ServerLevel level, BlockPos pos) {
    // FLATTENING make sure this not called during state changes
    EnumSet.allOf(Charge.class).forEach(n -> n.network(level).removeNode(pos));
  }

  enum ConnectType {
    BLOCK, SLAB, TRACK, WIRE
  }

  /**
   * A ChargeSpec defines the electrical properties of the block.
   * 
   * @param connectType - this controls how our block will connect to other blocks. Many blocks can
   *        only connect in specific ways due to block shape.
   * @param losses - the cost of connecting this block to the charge network due to resistance
   *        losses, etc. Transformers are typically 0.5. Batteries 0.2-0.3. Wires 0.025. Tracks
   *        0.01. Generators 0. Consumers 0.1.
   * @param storageSpec - the storage specification for our block. Storage is optional.
   */
  record Spec(ConnectType connectType, float losses,
      @Nullable ChargeStorage.Spec storageSpec) {

    /**
     * Helper method for ChargeSpec map construction.
     */
    public static Map<Charge, Spec> make(Charge network, ConnectType connectType, float losses) {
      return Map.of(network, new Spec(connectType, losses));
    }

    /**
     * Helper method for ChargeSpec map construction.
     */
    public static Map<Charge, Spec> make(Charge network, ConnectType connectType,
        float losses, @Nullable ChargeStorage.Spec storageSpec) {
      return Map.of(network, new Spec(connectType, losses, storageSpec));
    }

    public Spec(ConnectType connectType, float losses) {
      this(connectType, losses, null);
    }
  }
}
