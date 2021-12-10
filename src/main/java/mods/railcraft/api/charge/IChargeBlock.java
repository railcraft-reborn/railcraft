/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import com.google.common.base.Objects;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;

/**
 * This interface must be implement by any {@link net.minecraft.block.Block Block}
 * that wants to interface with any of the charge networks.
 *
 * <p>Created by CovertJaguar on 7/25/2016 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public interface IChargeBlock {

  /**
   * Asks the Block to provide a map of ChargeSpecs for each network.
   *
   * <p>It is generally to be considered an error to return the same charge definition to multiple
   * networks. Most blocks will probably be members of the {@link Charge#distribution} network only.
   *
   * <p>Only "transformer" blocks that pass charge from one network to another should respond to
   * multiple networks.
   *
   * @return A mapping of networks to ChargeSpecs. Most blocks should only respond to one type of
   *         network or an empty map. If an empty map is returned, the charge networks will ignore
   *         the block.
   */
  default Map<Charge, ChargeSpec> getChargeSpecs(BlockState state, BlockGetter world,
      BlockPos pos) {
    return Collections.emptyMap();
  }

  /**
   * The Charge Meter calls this to get access for meter readings.
   *
   * <p>Most blocks don't need to touch this, but Multi-blocks may want to redirect to the master
   * block.
   */
  default @Nullable Charge.IAccess getMeterAccess(Charge network, BlockState state, Level world,
      BlockPos pos) {
    return network.network(world).access(pos);
  }

  /**
   * Helper method for registering a block to the network.
   *
   * <p>This function must be called from the following functions:
   * {@link net.minecraft.block.Block#onBlockAdded(World, BlockPos, BlockState)}
   * {@link net.minecraft.block.Block#updateTick(World, BlockPos, BlockState, Random)}
   *
   * <p>The block must set {@link net.minecraft.block.Block#setTickRandomly(boolean)} to true in the
   * constructor.
   */
  default void registerNode(BlockState state, Level world, BlockPos pos) {
    EnumSet.allOf(Charge.class).forEach(n -> n.network(world).addNode(pos, state));
  }

  /**
   * Helper method for removing a block from the networks.
   *
   * <p>This function must be called from the following function:
   * {@link net.minecraft.block.Block#breakBlock(World, BlockPos, BlockState)}
   */
  default void deregisterNode(Level world, BlockPos pos) {
    // FLATTENING make sure this not called during state changes
    EnumSet.allOf(Charge.class).forEach(n -> n.network(world).removeNode(pos));
  }

  enum ConnectType {
    BLOCK, SLAB, TRACK, WIRE
  }

  /**
   * A ChargeSpec defines the electrical properties of the block.
   */
  final class ChargeSpec {
    private final ConnectType connectType;
    private final double losses;
    private final @Nullable IBatteryBlock.Spec batterySpec;

    /**
     * Helper method for ChargeSpec map construction.
     */
    public static Map<Charge, ChargeSpec> make(Charge network, ConnectType connectType,
        double losses) {
      return Collections.singletonMap(network, new ChargeSpec(connectType, losses));
    }

    /**
     * Helper method for ChargeSpec map construction.
     */
    public static Map<Charge, ChargeSpec> make(Charge network, ConnectType connectType,
        double losses, @Nullable IBatteryBlock.Spec batterySpec) {
      return Collections.singletonMap(network, new ChargeSpec(connectType, losses, batterySpec));
    }

    public ChargeSpec(ConnectType connectType, double losses) {
      this(connectType, losses, null);
    }

    /**
     * Creates a new charge specification.
     * @param connectType This controls how our block will connect to other blocks. Many blocks can
     *        only connect in specific ways due to block shape.
     * @param losses The cost of connecting this block to the charge network due to resistance
     *        losses, etc. Transformers are typically 0.5. Batteries 0.2-0.3. Wires 0.025. Tracks
     *        0.01. Generators 0. Consumers 0.1.
     * @param batterySpec The battery specification for our block. Batteries are optional.
     */
    public ChargeSpec(ConnectType connectType, double losses,
        @Nullable IBatteryBlock.Spec batterySpec) {
      this.connectType = connectType;
      this.losses = losses;
      this.batterySpec = batterySpec;
    }

    public double getLosses() {
      return losses;
    }

    public @Nullable IBatteryBlock.Spec getBatterySpec() {
      return batterySpec;
    }

    public ConnectType getConnectType() {
      return connectType;
    }

    @Override
    public String toString() {
      String string = String.format("ChargeSpec{%s, losses=%.2f}", connectType, losses);
      if (batterySpec != null) {
        string += "|" + batterySpec;
      }
      return string;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ChargeSpec that = (ChargeSpec) o;
      return Double.compare(that.losses, losses) == 0
          && connectType == that.connectType
          && Objects.equal(batterySpec, that.batterySpec);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(connectType, losses, batterySpec);
    }
  }
}
