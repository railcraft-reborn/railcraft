/*------------------------------------------------------------------------------
 Copyright (c) Railcraft Reborn, 2023+

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/
package mods.railcraft.api.charge;

import java.util.Objects;
import java.util.Optional;
import org.jetbrains.annotations.ApiStatus;
import com.google.common.base.Preconditions;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * The heart of the Charge system is here.
 *
 * <p>
 * Any block that wants to interact with the Charge network should implement {@link ChargeBlock} and
 * ensure that they call the proper add/remove functions.
 *
 * <p>
 * Everything else is done through {@link Access}.
 *
 * <p>
 * Example code: {@code Charge.distribution.network(world).access(pos).useCharge(500.0)}
 *
 *
 * <p>
 * General Charge Network Overview: The Charge Network is unique in that component blocks aren't
 * required to have a {@link net.minecraft.world.level.block.EntityBlock}.
 *
 * <p>
 * This means the vast majority of the network is made up of dumb static blocks resulting in very
 * low cpu overheard. This remains true even for very large grids. The network of grids is
 * maintained as a separate data structure outside the world. Each grid, which is defined as a
 * collection of connected charge blocks, only ticks its battery objects. Only blocks which store or
 * provide Charge to the grid need battery blocks; wires, tracks and consumers do not. This means
 * that only a small percentage of the grid is using cpu resources, while the bulk exists passively
 * simply as means to define connectivity.
 *
 * <p>
 * One side effect of this is that the Charge API cannot use the Forge's Capability API. That only
 * works on Tile Entities, not blocks. So don't bother asking for one, it won't happen.
 *
 * <p>
 * The grid is constructed lazily as things access it. It will slowly expand from there at a rate of
 * a few hundred blocks per tick per network. Grids form and grow and merge, a relatively cheap
 * operation, as they encounter other grids. However, any time a block is removed from the grid, the
 * entire grid is destroyed and must reform. So removal is the more expensive operation, avoid it if
 * possible. It is not recommended for blocks to remove themselves from the network for any reason
 * other than destruction. Block removal from the network could disrupt the grid its connected too
 * for several ticks, this may only be noticeable on grids of over several thousand blocks.
 *
 * <p>
 * Every block on the grid has a generic loss over time. It various with the type of block, more
 * details on that in the {link {@link mods.railcraft.api.charge.ChargeBlock.Spec}}. The value is
 * calculated as the grid is constructed and removed from the grid every tick. Consider it
 * representative of resistive losses and current leakage. All large scale real life power systems
 * suffer from these loss effects and are often major concerns when designing these systems.
 *
 * <p>
 * When a consumer asks to remove Charge from the grid, it goes to the list of batteries and tries
 * to remove from each in turn. The batteries are sorted based on their {@link ChargeStorage.State}.
 * The order batteries are drawn from is as such: source->rechargeable->disposable. Additionally,
 * they are further sorted based on efficiency. Each battery has an efficiency value associated with
 * it. This efficiency value defines how expensive it is to extract charge from the battery.
 * Generators have perfect efficiency and are more efficient than batteries which are more efficient
 * than transformers. To get a more efficient grid, add more generators or high efficiency
 * batteries.
 *
 * <p>
 * Charge is added to the grid by grabbing a source battery and adding Charge to the battery
 * directly. Generally a block that provides a source battery will use a Tile Entity to handle
 * creating and adding Charge to its own battery. From there the grid handles distribution.
 *
 * <p>
 * This is achieved by leveling the Charge in all the rechargeable batteries in the grid every tick.
 * The benefit of this is that even if the grid is split apart for any reason, charge will be evenly
 * distributed to the component parts. Batteries store their charge levels in their own NBT file
 * alongside the world, this allows them to not rely on Tile Entities for serialization.
 *
 * <p>
 * This brings us to another side effect of maintaining grids and batteries outside the world. The
 * grid will continue to operation unhindered even if large parts of it exist in chunks that are
 * currently not loaded. At the moment, to get this benefit the entire grid needs to be loaded at
 * least once per restart, though not all at the same time. Research is being done on how difficult
 * it would be to persist nodes as well as batteries. But that is an enhancement for the future.
 *
 * <p>
 * The unit of measurement used for Charge is now based on Forge Energy. This means, ignoring
 * efficiency losses, that 1 Charge equals 1 FE/RF. This allows for simple conversion between the
 * two systems. However, as a simplification, Railcraft's Charge lacks a concept of voltage. There
 * are however plans for separate transmission and distribution networks, which would result in
 * similar needs for transformers to convert from one to the other. You will see traces of these new
 * features scattered throughout this API. The framework is in place, it just lacks the blocks
 * themselves to make it work.
 *
 * <p>
 * As of 2021, Charge changed from 1 Charge unit to 1 IC2 EU to 1 Charge unit to 1 FE/RF
 *
 */
public enum Charge implements StringRepresentable {
  /**
   * The distribution network is the charge network used by standard consumers, wires, tracks, and
   * batteries.
   *
   * <p>
   * This is the only network currently implemented and currently covers all use cases.
   */
  distribution("distribution");
  /**
   * The transmission network is the charge network used by low maintenance transmission lines and
   * transformers, consumers should not access this network directly.
   *
   * <h3>Not currently implemented.</h3>
   */
  // transmission("transmission"),
  /**
   * The rail network is the charge network used by tracks and the carts on them.
   *
   * <h3>Not currently implemented.</h3>
   */

  // rail("rail"),
  /**
   * The catenary network is the charge network used by catenaries and the carts below them.
   *
   * <h3>Not currently implemented.</h3>
   */
  // catenary("catenary");

  private static ZapEffectProvider zapEffectProvider;

  private final String name;

  private Provider provider;

  private Charge(String name) {
    this.name = name;
  }

  @Override
  public String getSerializedName() {
    return this.name;
  }

  @Override
  public String toString() {
    return this.name;
  }

  @ApiStatus.Internal
  public void _setProvider(Provider provider) {
    Preconditions.checkState(this.provider == null, "provider already set.");
    this.provider = provider;
  }

  /**
   * This is how you get access to the meat of the charge network.
   */
  public Charge.Network network(ServerLevel level) {
    Objects.requireNonNull(this.provider);
    return this.provider.network(level);
  }

  /**
   * Entry point for rendering charge related effects.
   */
  public static ZapEffectProvider zapEffectProvider() {
    Objects.requireNonNull(zapEffectProvider);
    return zapEffectProvider;
  }

  @ApiStatus.Internal
  public static void _setZapEffectProvider(ZapEffectProvider zapEffectProvider) {
    Preconditions.checkState(Charge.zapEffectProvider == null, "zapEffectProvider already set.");
    Charge.zapEffectProvider = zapEffectProvider;
  }

  public interface Provider {

    /**
     * The network is the primary means of interfacing with charge. There is one network per game
     * world/dimentions.
     */
    Network network(ServerLevel level);
  }

  public interface Network {

    /**
     * Queues the node to be added to the network.
     *
     * @return true if the network changed.
     */
    boolean addNode(BlockPos blockPos, BlockState blockState);

    /**
     * Queues the node to be removed to the network.
     */
    void removeNode(BlockPos blockPos);

    /**
     * Get a grid access point for the position.
     *
     * @return A grid access point, may be a dummy object if there is no valid grid at the location.
     */
    Access access(BlockPos blockPos);
  }

  /**
   * The access interface used onto interacting with a network.
   */
  public interface Access {
    /**
     * Returns whether the network contains the requested charge amount and enough excess charge to
     * extract it.
     *
     * <p>
     * This operation takes into account the grid's efficiency value.
     *
     * @return true if there is enough charge in the network to withdraw the requested amount.
     */
    default boolean hasCapacity(int amount) {
      return false;
    }

    /**
     * Remove the requested amount of charge if possible and returns whether sufficient charge was
     * available to perform the operation.
     *
     * @param amount The total amount to pull from the grid.
     * @param simulate If TRUE, the extraction will only be simulated.
     * @return true if charge could be removed in full
     * @see Charge.Access#removeCharge
     */
    default boolean useCharge(int amount, boolean simulate) {
      return false;
    }

    /**
     * Removes as much of the desiredAmount of charge as possible from the grid.
     *
     * @param amount The total amount to pull from the grid.
     * @param simulate If TRUE, the extraction will only be simulated.
     * @return amount removed, may be less than desiredAmount
     * @see Charge.Access#useCharge
     */
    default int removeCharge(int amount, boolean simulate) {
      return 0;
    }

    /**
     * Get the node's storage object. Don't hold onto this reference, just grab it from the network
     * as needed.
     *
     * @return The storage object.
     */
    default Optional<? extends ChargeStorage> storage() {
      return Optional.empty();
    }

    /**
     * Can be returned from
     * <p>
     * {@link net.minecraft.world.level.block.state.BlockBehaviour#getAnalogOutputSignal(BlockState, Level, BlockPos)}
     *
     * @return The current storage percentage of the entire grid.
     */
    default int getComparatorOutput() {
      return 0;
    }

    /**
     * Apply Charge damage to the target entity from the current network.
     */
    default void zap(Entity entity, DamageOrigin origin, float damage) {
      return;
    }
  }

  public enum DamageOrigin {
    BLOCK, TRACK
  }

  /**
   * Interface used by client particles.
   */
  public interface ZapEffectProvider {

    /**
     * Helper method that most blocks can use for spark effects. It has a chance of calling
     * {@link #zapEffectSurface(BlockState, Level, BlockPos)}.
     *
     * <p>
     * The chance is increased if it's raining.
     *
     * @param chance Integer value such that chance of sparking is defined by
     *        {@code rand.nextInt(chance) == 0} Most blocks use 50, tracks use 75. Lower numbers
     *        means more frequent sparks.
     */
    default void throwSparks(BlockState state, Level level, BlockPos pos, RandomSource rand,
        int chance) {}

    /**
     * Spawns a single spark from a point source.
     */
    default void zapEffectPoint(Level level, double x, double y, double z) {}

    /**
     * Spawns a spark from the surface of each rendered side of a block.
     */
    default void zapEffectSurface(BlockState state, Level level, BlockPos pos) {}

    /**
     * Spawns a lot of sparks from a point source.
     */
    default void zapEffectDeath(Level level, double x, double y, double z) {}
  }
}
