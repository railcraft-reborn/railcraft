/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import java.util.Optional;
import java.util.Random;
import com.google.common.annotations.Beta;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

/**
 * The heart of the Charge system is here.
 *
 * Any block that wants to interact with the Charge network should implement {@link IChargeBlock}
 * and ensure that they call the proper add/remove functions.
 *
 * Everything else is done through {@link IAccess}.
 *
 * Example code: {@code Charge.distribution.network(world).access(pos).useCharge(500.0)}
 *
 *
 * General Charge Network Overview: ------------------- The Charge Network is unique in that
 * component blocks aren't required to have a {@link net.minecraft.tileentity.TileEntity}.
 *
 * This means the vast majority of the network is made up of dumb static blocks resulting in very
 * low cpu overheard. This remains true even for very large grids. The network of grids is
 * maintained as a separate data structure outside the world. Each grid, which is defined as a
 * collection of connected charge blocks, only ticks its battery objects. Only blocks which store or
 * provide Charge to the grid need battery blocks; wires, tracks and consumers do not. This means
 * that only a small percentage of the grid is using cpu resources, while the bulk exists passively
 * simply as means to define connectivity.
 *
 * One side effect of this is that the Charge API cannot use the Forge's Capability API. That only
 * works on Tile Entities, not blocks. So don't bother asking for one, it won't happen.
 *
 * The grid is constructed lazily as things access it. It will slowly expand from there at a rate of
 * a few hundred blocks per tick per network. Grids form and grow and merge, a relatively cheap
 * operation, as they encounter other grids. However, any time a block is removed from the grid, the
 * entire grid is destroyed and must reform. So removal is the more expensive operation, avoid it if
 * possible. It is not recommended for blocks to remove themselves from the network for any reason
 * other than destruction. Block removal from the network could disrupt the grid its connected too
 * for several ticks, this may only be noticeable on grids of over several thousand blocks.
 *
 * Every block on the grid has a generic loss over time. It various with the type of block, more
 * details on that in the {link {@link mods.railcraft.api.charge.IChargeBlock.ChargeSpec}}. The
 * value is calculated as the grid is constructed and removed from the grid every tick. Consider it
 * representative of resistive losses and current leakage. All large scale real life power systems
 * suffer from these loss effects and are often major concerns when designing these systems.
 *
 * When a consumer asks to remove Charge from the grid, it goes to the list of batteries and tries
 * to remove from each in turn. The batteries are sorted based on their {@link IBatteryBlock.State}.
 * The order batteries are drawn from is as such: source->rechargeable->disposable. Additionally
 * they are further sorted based on efficiency. Each battery has an efficiency value associated with
 * it. This efficiency value defines how expensive it is to extract charge from the battery.
 * Generators have perfect efficiency and are more efficient than batteries which are more efficient
 * than transformers. To get a more efficient grid, add more generators or high efficiency
 * batteries.
 *
 * Charge is added to the grid by grabbing a source battery and adding Charge to the battery
 * directly. Generally a block that provides a source battery will use a Tile Entity to handle
 * creating and adding Charge to its own battery. From there the grid handles distribution.
 *
 * This is achieved by leveling the Charge in all the rechargeable batteries in the grid every tick.
 * The benefit of this is that even if the grid is split apart for any reason, charge will be evenly
 * distributed to the component parts. Batteries store their charge levels in their own NBT file
 * alongside the world, this allows them to not rely on Tile Entities for serialization.
 *
 * This brings us to another side effect of maintaining grids and batteries outside the world. The
 * grid will continue to operation unhindered even if large parts of it exist in chunks that are
 * currently not loaded. At the moment, to get this benefit the entire grid needs to be loaded at
 * least once per restart, though not all at the same time. Research is being done on how difficult
 * it would be to persist nodes as well as batteries. But that is an enhancement for the future.
 *
 * The unit of measurement used for Charge is based on IndustrialCraft2's Energy Units. This means,
 * ignoring efficiency losses, that 1 Charge equals 1 EU. This allows for simple conversion between
 * the two systems. However, as a simplification, Railcraft's Charge lacks a concept of voltage.
 * There are however plans for separate transmission and distribution networks, which would result
 * in similar needs for transformers to convert from one to the other. You will see traces of these
 * new features scattered throughout this API. The framework is in place, it just lacks the blocks
 * themselves to make it work.
 *
 * Created by CovertJaguar on 10/19/2018 for Railcraft.
 *
 * @author CovertJaguar <https://www.railcraft.info>
 */
public enum Charge {
  /**
   * The distribution network is the charge network used by standard consumers, wires, tracks, and
   * batteries.
   *
   * This is the only network currently implemented and currently covers all use cases.
   */
  distribution,
  /**
   * The transmission network is the charge network used by low maintenance transmission lines and
   * transformers, consumers should not access this network directly.
   *
   * Not currently implemented.
   */
  @Beta
  transmission,
  /**
   * The rail network is the charge network used by tracks and the carts on them.
   *
   * Not currently implemented.
   */
  @Beta
  rail,
  /**
   * The catenary network is the charge network used by catenaries and the carts below them.
   *
   * Not currently implemented.
   */
  @Beta
  catenary;

  /**
   * This is how you get access to the meat of the charge network.
   *
   * @throws mods.railcraft.api.core.ClientAccessException if you call it from the client thread.
   */
  public Charge.Network network(IWorld world) {
    return manager.network(world);
  }

  /**
   * Entry point for rendering charge related effects.
   */
  public static IZapEffectRenderer effects() {
    return effects;
  }

  /**
   * Entry point for charge related effects sent from the server thread.
   */
  public static IHostZapEffect hostEffects() {
    return hostEffects;
  }

  public interface IManager {

    /**
     * The network is the primary means of interfacing with charge.
     */
    default Network network(IWorld world) {
      return new Network() {};
    }
  }

  /**
   * Created by CovertJaguar on 10/19/2018 for Railcraft.
   *
   * @author CovertJaguar <https://www.railcraft.info>
   */
  public interface Network {

    /**
     * Queues the node to be added to the network.
     *
     * If you pass a null chargeDef, nothing will happen.
     *
     * @return return true if the network changed.
     */
    default boolean addNode(BlockPos pos, BlockState state) {
      return false;
    }

    /**
     * Queues the node to be removed to the network
     */
    default void removeNode(BlockPos pos) {}

    /**
     * Get a grid access point for the position.
     *
     * @return A grid access point, may be a dummy object if there is no valid grid at the location.
     */
    default IAccess access(BlockPos pos) {
      return new IAccess() {};
    }

  }

  /**
   * Created by CovertJaguar on 11/2/2018 for Railcraft.
   *
   * @author CovertJaguar <https://www.railcraft.info>
   */
  public interface IAccess {
    /**
     * Returns whether the network contains the requested charge amount and enough excess charge to
     * extract it.
     *
     * This operation takes into account the grid's efficiency value.
     *
     * @return true if there is enough charge in the network to withdraw the requested amount.
     */
    default boolean hasCapacity(double amount) {
      return false;
    }

    /**
     * Remove the requested amount of charge if possible and returns whether sufficient charge was
     * available to perform the operation.
     *
     * @return true if charge could be removed in full
     */
    default boolean useCharge(double amount) {
      return false;
    }

    /**
     * Removes as much of the desiredAmount of charge as possible from the gird.
     *
     * @return amount removed, may be less than desiredAmount
     */
    default double removeCharge(double desiredAmount) {
      return 0.0;
    }

    /**
     * Get the node's battery object.
     *
     * Don't hold onto this reference, just grab it from the network as needed.
     *
     * @return The battery object.
     */
    default Optional<? extends IBatteryBlock> getBattery() {
      return Optional.empty();
    }

    /**
     * Can be returned from
     * {@link net.minecraft.block.Block#getComparatorInputOverride(BlockState, World, BlockPos)}.
     *
     * @return The current storage percentage of the entire grid.
     */
    default int getComparatorOutput() {
      return 0;
    }

    /**
     * Apply Charge damage to the target entity from the current network.
     */
    default void zap(Entity entity, DamageOrigin origin, float damage) {}

  }

  public enum DamageOrigin {
    BLOCK, TRACK
  }

  public interface IHostZapEffect {
    /**
     * Spawns a lot of sparks from a point source.
     *
     * @param source Can be a TileEntity, Entity, BlockPos, or Vector3d
     * @throws IllegalArgumentException If source is of an unexpected type.
     */
    default void zapEffectDeath(World world, Object source) {}
  }

  public interface IZapEffectRenderer {
    /**
     * Helper method that most blocks can use for spark effects. It has a chance of calling
     * {@link #zapEffectSurface(BlockState, World, BlockPos)}.
     *
     * The chance is increased if its raining.
     *
     * @param chance Integer value such that chance of sparking is defined by
     *        {@code rand.nextInt(chance) == 0} Most blocks use 50, tracks use 75. Lower numbers
     *        means more frequent sparks.
     */
    default void throwSparks(BlockState state, World world, BlockPos pos, Random rand,
        int chance) {}

    /**
     * Spawns a single spark from a point source.
     *
     * @param source Can be a TileEntity, Entity, BlockPos, or Vector3d
     * @throws IllegalArgumentException If source is of an unexpected type.
     */
    default void zapEffectPoint(World world, Vector3d source) {}

    /**
     * Spawns a lot of sparks from a point source.
     *
     * @param source Can be a TileEntity, Entity, BlockPos, or Vector3d
     * @throws IllegalArgumentException If source is of an unexpected type.
     */
    default void zapEffectDeath(World world, Vector3d source) {}

    /**
     * Spawns a spark from the surface of each rendered side of a block.
     */
    default void zapEffectSurface(BlockState stateIn, World worldIn, BlockPos pos) {}
  }

  /**
   * User's shouldn't touch this. It's set using reflection by Railcraft.
   */
  private IManager manager = new IManager() {};

  /**
   * User's shouldn't touch this. It's set using reflection by Railcraft.
   */
  private static IZapEffectRenderer effects = new IZapEffectRenderer() {};

  public static void internalSetEffects(IZapEffectRenderer effects) {
    Charge.effects = effects;
  }

  private static IHostZapEffect hostEffects = new IHostZapEffect() {};

  public static void internalSetHostEffects(IHostZapEffect hostEffects) {
    Charge.hostEffects = hostEffects;
  }
}
