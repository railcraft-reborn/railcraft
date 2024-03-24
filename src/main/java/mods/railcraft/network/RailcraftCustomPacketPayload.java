package mods.railcraft.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public interface RailcraftCustomPacketPayload extends CustomPacketPayload {
  void handle(PlayPayloadContext context);

  default void handleMainThread(PlayPayloadContext context) {
    context.workHandler().execute(() -> handle(context));
  }
}
