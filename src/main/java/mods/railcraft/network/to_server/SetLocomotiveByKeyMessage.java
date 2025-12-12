package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.vehicle.minecart.Minecart;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetLocomotiveByKeyMessage(LocomotiveKeyBinding keyBinding) implements CustomPacketPayload {

  public static final Type<SetLocomotiveByKeyMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_locomotive_by_key"));

  public static final StreamCodec<FriendlyByteBuf, SetLocomotiveByKeyMessage> STREAM_CODEC =
      StreamCodec.composite(
          NeoForgeStreamCodecs.enumCodec(LocomotiveKeyBinding.class), SetLocomotiveByKeyMessage::keyBinding,
          SetLocomotiveByKeyMessage::new);

  public enum LocomotiveKeyBinding {
    REVERSE,
    FASTER,
    SLOWER,
    MODE_CHANGE,
    WHISTLE;
  }

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetLocomotiveByKeyMessage message, IPayloadContext context) {
    var player = context.player();
    if (!(player.getVehicle() instanceof Minecart minecart)) {
      return;
    }
    switch (message.keyBinding) {
      case REVERSE -> Locomotive.applyAction(player, minecart, false, loco -> loco.setReverse(!loco.isReverse()));
      case FASTER -> Locomotive.applyAction(player, minecart, false, Locomotive::increaseSpeed);
      case SLOWER -> Locomotive.applyAction(player, minecart, false, Locomotive::decreaseSpeed);
      case WHISTLE -> Locomotive.applyAction(player, minecart, false, Locomotive::whistle);
      case MODE_CHANGE -> Locomotive.applyAction(player, minecart, false, loco -> {
        var mode = loco.getMode();
        do {
          mode = mode.next();
        } while (!loco.isAllowedMode(mode));
        loco.setMode(mode);
      });
    }
  }
}
