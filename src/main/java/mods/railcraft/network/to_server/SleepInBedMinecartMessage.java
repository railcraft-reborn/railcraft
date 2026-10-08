package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.BedMinecart;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SleepInBedMinecartMessage() implements CustomPacketPayload {

  public static final Type<SleepInBedMinecartMessage> TYPE =
      new Type<>(RailcraftConstants.rl("sleep_in_bed_minecart"));

  public static final StreamCodec<FriendlyByteBuf, SleepInBedMinecartMessage> STREAM_CODEC =
      StreamCodec.unit(new SleepInBedMinecartMessage());

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SleepInBedMinecartMessage message, IPayloadContext context) {
    if (context.player() instanceof ServerPlayer player
        && player.getVehicle() instanceof BedMinecart cart) {
      cart.attemptSleep(player);
    }
  }
}
