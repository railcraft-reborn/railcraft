package mods.railcraft.network.play;

import java.util.List;
import java.util.function.Supplier;
import mods.railcraft.client.gui.screen.inventory.LogBookScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record OpenLogBookScreen(List<List<String>> pages) {

  public void encode(FriendlyByteBuf out) {
    out.writeCollection(pages, (friendlyByteBuf, strings) ->
        friendlyByteBuf.writeCollection(strings, (FriendlyByteBuf::writeUtf)));
  }

  public static OpenLogBookScreen decode(FriendlyByteBuf in) {
    return new OpenLogBookScreen(
        in.readList(friendlyByteBuf ->friendlyByteBuf.readList(FriendlyByteBuf::readUtf)));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    Minecraft.getInstance().setScreen(new LogBookScreen(pages));
    return true;
  }
}
