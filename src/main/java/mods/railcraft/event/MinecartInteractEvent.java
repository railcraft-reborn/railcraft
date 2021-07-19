package mods.railcraft.event;

import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

@Cancelable
public class MinecartInteractEvent extends Event {

  private final AbstractMinecartEntity cart;
  private final PlayerEntity player;
  private final Hand hand;

  public MinecartInteractEvent(AbstractMinecartEntity cart, PlayerEntity player, Hand hand) {
    this.cart = cart;
    this.player = player;
    this.hand = hand;
  }

  public AbstractMinecartEntity getCart() {
    return this.cart;
  }

  public PlayerEntity getPlayer() {
    return this.player;
  }

  public Hand getHand() {
    return this.hand;
  }
}
