/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.event;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
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

  private final AbstractMinecartEntity one;
  private final AbstractMinecartEntity two;

  protected CartLinkEvent(AbstractMinecartEntity one, AbstractMinecartEntity two) {
    this.one = one;
    this.two = two;
  }

  public AbstractMinecartEntity getCartOne() {
    return this.one;
  }

  public AbstractMinecartEntity getCartTwo() {
    return this.two;
  }

  public static final class Link extends CartLinkEvent {

    public Link(AbstractMinecartEntity one, AbstractMinecartEntity two) {
      super(one, two);
    }
  }

  public static final class Unlink extends CartLinkEvent {

    public Unlink(AbstractMinecartEntity one, AbstractMinecartEntity two) {
      super(one, two);
    }
  }
}
