package mods.railcraft.network.to_client;

import java.util.List;
import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.client.ScreenFactories;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record OpenLogBookScreen(List<List<String>> pages) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("open_log_book");

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

  @Override
  public void handle(PlayPayloadContext context) {
    ScreenFactories.openLogBookScreen(pages);
  }
}
