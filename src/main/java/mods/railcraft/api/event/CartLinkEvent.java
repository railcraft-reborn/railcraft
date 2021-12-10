/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.event;

import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.eventbus.api.Event;

/**
 * These events are fired on {@link net.minecraftforge.common.MinecraftForge#EVENT_BUS}.
 *
 * <p>
 * In order to cancel linking, call
 * {@link mods.railcraft.api.carts.LinkageManager#breakLink(AbstractMinecartEntity, AbstractMinecartEntity)}
 * </p>
 */
public class CartLinkEvent extends Event {

  private final AbstractMinecart one;
  private final AbstractMinecart two;

  protected CartLinkEvent(AbstractMinecart one, AbstractMinecart two) {
    this.one = one;
    this.two = two;
  }

  public AbstractMinecart getCartOne() {
    return this.one;
  }

  public AbstractMinecart getCartTwo() {
    return this.two;
  }

  public static final class Link extends CartLinkEvent {

    public Link(AbstractMinecart one, AbstractMinecart two) {
      super(one, two);
    }
  }

  public static final class Unlink extends CartLinkEvent {

    public Unlink(AbstractMinecart one, AbstractMinecart two) {
      super(one, two);
    }
  }
}
