package mods.railcraft.network.to_client;

import java.util.List;
import mods.railcraft.Railcraft;
import mods.railcraft.client.ScreenFactories;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record OpenLogBookScreen(List<List<String>> pages) implements CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("open_log_book");

  public static OpenLogBookScreen read(FriendlyByteBuf buf) {
    return new OpenLogBookScreen(buf.readList(b -> b.readList(FriendlyByteBuf::readUtf)));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeCollection(pages, (b, strings) ->
        b.writeCollection(strings, (FriendlyByteBuf::writeUtf)));
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    ScreenFactories.openLogBookScreen(pages);
  }
}
