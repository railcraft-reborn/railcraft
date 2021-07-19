/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.events;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.eventbus.api.Event;

/**
 * @author CovertJaguar <http://www.railcraft.info>
 */
public abstract class CartLockdownEvent extends Event {

  public final AbstractMinecartEntity cart;
  public final BlockPos pos;

  CartLockdownEvent(AbstractMinecartEntity cart, BlockPos pos) {
    this.cart = cart;
    this.pos = pos;
  }

  /**
   * This event is posted every tick that a LockType Track (Lockdown, Holding, Boarding) is holding
   * onto a minecart.
   */
  public static final class Lock extends CartLockdownEvent {

    public Lock(AbstractMinecartEntity cart, BlockPos pos) {
      super(cart, pos);
    }
  }

  /**
   * This event is posted every tick that a LockType Track (Lockdown, Holding, Boarding) is
   * releasing a minecart.
   */
  public static final class Release extends CartLockdownEvent {

    public Release(AbstractMinecartEntity cart, BlockPos pos) {
      super(cart, pos);
    }
  }
}
