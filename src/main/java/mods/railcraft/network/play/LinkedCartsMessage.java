package mods.railcraft.network.play;

import java.util.Collection;
import java.util.UUID;
import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.client.ClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.neoforge.network.NetworkEvent;

public record LinkedCartsMessage(Collection<LinkedCart> linkedCarts) {

  public void encode(FriendlyByteBuf out) {
    out.writeCollection(this.linkedCarts, (buf, cart) -> cart.encode(buf));
  }

  public static LinkedCartsMessage decode(FriendlyByteBuf in) {
    return new LinkedCartsMessage(in.readList(LinkedCart::decode));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ClientManager.getShuntingAuraRenderer().setLinkedCarts(this.linkedCarts);
    return true;
  }

  public record LinkedCart(int entityId, @Nullable UUID trainId, int linkAId, int linkBId) {

    public LinkedCart(RollingStock extension) {
      this(
          extension.entity().getId(),
          extension.train().id(),
          extension.linkAt(Side.BACK)
              .map(RollingStock::entity)
              .map(AbstractMinecart::getId)
              .orElse(-1),
          extension.linkAt(Side.FRONT)
              .map(RollingStock::entity)
              .map(AbstractMinecart::getId)
              .orElse(-1));
    }

    public void encode(FriendlyByteBuf out) {
      out.writeVarInt(this.entityId);
      out.writeNullable(this.trainId, FriendlyByteBuf::writeUUID);
      out.writeVarInt(this.linkAId);
      out.writeVarInt(this.linkBId);
    }

    public static LinkedCart decode(FriendlyByteBuf in) {
      return new LinkedCart(
          in.readVarInt(),
          in.readNullable(FriendlyByteBuf::readUUID),
          in.readVarInt(),
          in.readVarInt());
    }
  }
}
