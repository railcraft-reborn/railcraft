package mods.railcraft.network.play;

import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class LinkedCartsMessage {

  private final LinkedCart[] linkedCarts;

  public LinkedCartsMessage(LinkedCart[] linkedCart) {
    this.linkedCarts = linkedCart;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.linkedCarts.length);
    for (LinkedCart cart : this.linkedCarts) {
      out.writeVarInt(cart.getEntityId());
      if (cart.getTrainId() == null) {
        out.writeBoolean(true);
      } else {
        out.writeBoolean(false);
        out.writeUUID(cart.getTrainId());
      }
      out.writeUUID(cart.getLinkAId());
      out.writeUUID(cart.getLinkBId());
    }
  }

  public static LinkedCartsMessage decode(PacketBuffer in) {
    LinkedCart[] linkedCarts = new LinkedCart[in.readVarInt()];
    for (int i = 0; i < linkedCarts.length; i++) {
      linkedCarts[i] = new LinkedCart(in.readVarInt(), in.readBoolean() ? null : in.readUUID(),
          in.readUUID(), in.readUUID());
    }
    return new LinkedCartsMessage(linkedCarts);
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    Railcraft.getInstance().getClientDist().getShuntingAuraRenderer()
        .setLinkedCarts(this.linkedCarts);
  }

  public static class LinkedCart {

    private final int entityId;
    @Nullable
    private final UUID trainId;
    private final UUID linkAId;
    private final UUID linkBId;

    public LinkedCart(int entityId, @Nullable UUID trainId, UUID linkAId, UUID linkBId) {
      this.entityId = entityId;
      this.trainId = trainId;
      this.linkAId = linkAId;
      this.linkBId = linkBId;
    }

    public int getEntityId() {
      return this.entityId;
    }

    public UUID getTrainId() {
      return this.trainId;
    }

    public UUID getLinkAId() {
      return this.linkAId;
    }

    public UUID getLinkBId() {
      return this.linkBId;
    }
  }
}
