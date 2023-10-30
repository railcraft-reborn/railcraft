package mods.railcraft.network.play;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.client.ScreenFactories;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record OpenLogBookScreen(List<List<String>> pages) {

  public void encode(FriendlyByteBuf out) {
    out.writeCollection(pages, (out1, strings) ->
        out1.writeCollection(strings, (FriendlyByteBuf::writeUtf)));
  }

  public static OpenLogBookScreen decode(FriendlyByteBuf in) {
    return new OpenLogBookScreen(in.readList(in1 -> in1.readList(FriendlyByteBuf::readUtf)));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ScreenFactories.openLogBookScreen(pages);
    return true;
  }
}
