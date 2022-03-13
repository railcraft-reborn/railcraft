package mods.railcraft.network.play;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.world.entity.vehicle.Link;
import mods.railcraft.world.entity.vehicle.MinecartExtension;
import mods.railcraft.world.entity.vehicle.Train;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraftforge.network.NetworkEvent;

public class LinkedCartsMessage {

  private final Collection<LinkedCart> linkedCarts;

  public LinkedCartsMessage(Collection<LinkedCart> linkedCart) {
    this.linkedCarts = linkedCart;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeCollection(this.linkedCarts, (buf, cart) -> cart.encode(buf));
  }

  public static LinkedCartsMessage decode(FriendlyByteBuf in) {
    return new LinkedCartsMessage(in.readList(LinkedCart::decode));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    Railcraft.getInstance().getClientDist().getShuntingAuraRenderer()
        .setLinkedCarts(this.linkedCarts);
    return true;
  }

  public static record LinkedCart(int entityId, @Nullable UUID trainId, int linkAId, int linkBId) {

    public LinkedCart(MinecartExtension extension) {
      this(extension.getMinecart().getId(), Train.getTrainUUID(extension.getMinecart()),
          extension.getLinkedMinecart(Link.FRONT).map(AbstractMinecart::getId).orElse(-1),
          extension.getLinkedMinecart(Link.BACK).map(AbstractMinecart::getId).orElse(-1));
    }

    public void encode(FriendlyByteBuf out) {
      out.writeVarInt(this.entityId);
      if (this.trainId == null) {
        out.writeBoolean(true);
      } else {
        out.writeBoolean(false);
        out.writeUUID(this.trainId);
      }
      out.writeVarInt(this.linkAId);
      out.writeVarInt(this.linkBId);
    }

    public static LinkedCart decode(FriendlyByteBuf in) {
      return new LinkedCart(in.readVarInt(), in.readBoolean() ? null : in.readUUID(),
          in.readVarInt(), in.readVarInt());
    }
  }
}
