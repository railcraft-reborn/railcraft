/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import com.google.common.base.Objects;

/**
 * Batteries the heart of the Charge Network.
 *
 * <p>Consumers and 'wires' don't need batteries, but generators and battery blocks do.
 *
 * <p>You don't to have a Tile Entity to provide a battery for the network,
 * serialization and ticking is handled by the network itself.
 *
 * <p>Generators should add their power output directly to its battery object.
 *
 * <p><b>You shouldn't hold onto battery objects for longer than you need them.</b>
 * The API makes no guarantee that the battery object assigned to a specific coordinate
 * will always be the same object.
 *
 *
 * <p>Such that sometimes:
 * <pre>
 * {@code
 * IBatteryBlock bat1 = Charge.distribution.network(world).access(pos).getBattery();
 * IBatteryBlock bat2 = Charge.distribution.network(world).access(pos).getBattery();
 * bat1 != bat2
 * }
 * </pre>
 *
 * <p>Created by CovertJaguar on 10/27/2018 for Railcraft.
 *
 * @author CovertJaguar (https://www.railcraft.info)
 */
public interface IBatteryBlock extends ChargeStorage {

  enum State {
    /**
     * Infinite Batteries will supply an infinite amount of power to the network.
     */
    INFINITE("tile.railcraft.battery.state.infinite"),
    /**
     * Source batteries are used in generators and transformers. The charge they hold will be used
     * to charge the rechargeable batteries in the network, but they themselves will not be load
     * balanced.
     */
    SOURCE("tile.railcraft.battery.state.source"),
    /**
     * Rechargeable batteries can be filled and drained indefinitely. The charge network will
     * balance the change level between all the rechargeable batteries in the network.
     *
     * <p>Generators should posses a small rechargeable battery just large enough to hold the
     * generator's max per tick output with a similar draw level and 100% efficiency.
     */
    RECHARGEABLE("tile.railcraft.battery.state.rechargeable"),
    /**
     * Disposable batteries are excluded from the charge network's level balancing. They will be
     * drained after rechargeable batteries.
     */
    DISPOSABLE("tile.railcraft.battery.state.disposable"),
    /**
     * Disabled batteries are ignored by the network. Use for redstone switching or multiblock
     * logic, etc.
     */
    DISABLED("tile.railcraft.battery.state.disabled");

    private State(String locTag) {}
  }

  /**
   * Gets the current state of the battery.
   *
   * @return The battery's state.
   */
  default State getState() {
    return State.RECHARGEABLE;
  }

  /**
   * Sets the current state of the battery.
   *
   * <p>The state of a battery is always under the control of the client,
   * the network will never change it for you.
   *
   * @param stateImpl The battery's new state.
   */
  default void setState(State stateImpl) {}

  class Spec {

    private final IBatteryBlock.State initialState;
    private final double capacity;
    private final double maxDraw;
    private final double efficiency;

    /**
     * Creates a new battery Spec.
     * @param initialState The initial state of the battery.
     * @param capacity The capacity of the battery.
     * @param maxDraw How much charge can be drawn from this battery per tick.
     * @param efficiency How efficient it is to draw from this battery.
     *
     *        Generators/Converters should generally have this set to 1.0. Other types of blocks
     *        will vary.
     */
    public Spec(IBatteryBlock.State initialState, double capacity, double maxDraw,
        double efficiency) {
      this.initialState = initialState;
      this.capacity = capacity;
      this.maxDraw = maxDraw;
      this.efficiency = efficiency;
    }

    public IBatteryBlock.State getInitialState() {
      return initialState;
    }

    public double getCapacity() {
      return capacity;
    }

    public double getMaxDraw() {
      return maxDraw;
    }

    public double getEfficiency() {
      return efficiency;
    }

    @Override
    public String toString() {
      return String.format("Battery{Cap: %.2f, Draw: %.2f, Eff: %.2f}", capacity, maxDraw,
          efficiency);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Spec spec = (Spec) o;
      return (Double.compare(spec.capacity, capacity) == 0)
          && (Double.compare(spec.maxDraw, maxDraw) == 0)
          && (Double.compare(spec.efficiency, efficiency) == 0)
          && (initialState == spec.initialState);
    }

    @Override
    public int hashCode() {
      return Objects.hashCode(initialState, capacity, maxDraw, efficiency);
    }
  }
}
