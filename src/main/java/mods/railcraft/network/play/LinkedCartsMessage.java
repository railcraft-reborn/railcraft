package mods.railcraft.network.play;

import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class LinkedCartsMessage {

  private final LinkedCart[] linkedCarts;

  public LinkedCartsMessage(LinkedCart[] linkedCart) {
    this.linkedCarts = linkedCart;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.linkedCarts.length);
    for (var cart : this.linkedCarts) {
      out.writeVarInt(cart.entityId());
      if (cart.trainId() == null) {
        out.writeBoolean(true);
      } else {
        out.writeBoolean(false);
        out.writeUUID(cart.trainId());
      }
      out.writeUUID(cart.linkAId());
      out.writeUUID(cart.linkBId());
    }
  }

  public static LinkedCartsMessage decode(FriendlyByteBuf in) {
    var linkedCarts = new LinkedCart[in.readVarInt()];
    for (var i = 0; i < linkedCarts.length; i++) {
      linkedCarts[i] = new LinkedCart(in.readVarInt(), in.readBoolean() ? null : in.readUUID(),
          in.readUUID(), in.readUUID());
    }
    return new LinkedCartsMessage(linkedCarts);
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    Railcraft.getInstance().getClientDist().getShuntingAuraRenderer()
        .setLinkedCarts(this.linkedCarts);
    return true;
  }

  public static record LinkedCart(int entityId, @Nullable UUID trainId, UUID linkAId,
      UUID linkBId) {}
}
