package mods.railcraft.network.to_client;

import java.util.Collection;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Railcraft;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.carts.Side;
import mods.railcraft.client.ClientManager;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record LinkedCartsMessage(
    Collection<LinkedCart> linkedCarts) implements CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("linked_carts");

  public static LinkedCartsMessage read(FriendlyByteBuf buf) {
    return new LinkedCartsMessage(buf.readList(LinkedCart::read));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeCollection(this.linkedCarts, (b, cart) -> cart.write(b));
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    ClientManager.getShuntingAuraRenderer().setLinkedCarts(this.linkedCarts);
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

    public static LinkedCart read(FriendlyByteBuf buf) {
      return new LinkedCart(
          buf.readVarInt(),
          buf.readNullable(FriendlyByteBuf::readUUID),
          buf.readVarInt(),
          buf.readVarInt());
    }

    public void write(FriendlyByteBuf buf) {
      buf.writeVarInt(this.entityId);
      buf.writeNullable(this.trainId, FriendlyByteBuf::writeUUID);
      buf.writeVarInt(this.linkAId);
      buf.writeVarInt(this.linkBId);
    }
  }
}
