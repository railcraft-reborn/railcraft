package mods.railcraft.network.to_client;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.ClientManager;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record LinkedCartsMessage(
    Collection<LinkedCart> linkedCarts) implements CustomPacketPayload {

  public static final Type<LinkedCartsMessage> TYPE =
      new Type<>(RailcraftConstants.id("linked_carts"));

  public static final StreamCodec<FriendlyByteBuf, LinkedCartsMessage> STREAM_CODEC =
      StreamCodec.composite(
          LinkedCart.STREAM_CODEC.apply(ByteBufCodecs.collection(ArrayList::new)), LinkedCartsMessage::linkedCarts,
          LinkedCartsMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(LinkedCartsMessage message, IPayloadContext context) {
    ClientManager.getShuntingAuraRenderer().setLinkedCarts(message.linkedCarts);
  }

  public record LinkedCart(int entityId, Optional<UUID> trainId, int linkAId, int linkBId) {

    private static final StreamCodec<FriendlyByteBuf, LinkedCart> STREAM_CODEC =
        StreamCodec.composite(
            ByteBufCodecs.VAR_INT, LinkedCart::entityId,
            ByteBufCodecs.optional(UUIDUtil.STREAM_CODEC), LinkedCart::trainId,
            ByteBufCodecs.VAR_INT, LinkedCart::linkAId,
            ByteBufCodecs.VAR_INT, LinkedCart::linkBId,
            LinkedCart::new);

    public LinkedCart(RollingStock extension) {
      this(
          extension.entity().getId(),
          Optional.of(extension.train().id()),
          extension.backLink()
              .map(RollingStock::entity)
              .map(AbstractMinecart::getId)
              .orElse(-1),
          extension.frontLink()
              .map(RollingStock::entity)
              .map(AbstractMinecart::getId)
              .orElse(-1));
    }
  }
}
