package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.network.PacketDispatcher;
import mods.railcraft.world.inventory.RailcraftMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetMenuStringMessage {

  private byte windowId, dataId;
  private String str;

  public SetMenuStringMessage(int windowId, int dataId, String str) {
    this.windowId = (byte) windowId;
    this.dataId = (byte) dataId;
    this.str = str;
  }

  public void sendPacket(ServerPlayerEntity player) {
    PacketDispatcher.sendToPlayer(this, player);
  }

  public void encode(PacketBuffer data) {
    data.writeByte(windowId);
    data.writeByte(dataId);
    data.writeUtf(str);
  }

  public static SetMenuStringMessage decode(PacketBuffer in) {
    return new SetMenuStringMessage(in.readByte(), in.readByte(), in.readUtf());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    context.get().enqueueWork(() -> processOnClient(this));
    return true;
  }

  private static void processOnClient(SetMenuStringMessage message) {
    Minecraft mc = Minecraft.getInstance();
    if (mc.player.containerMenu instanceof RailcraftMenu
        && mc.player.containerMenu.containerId == message.windowId) {
      ((RailcraftMenu) mc.player.containerMenu).updateString(message.dataId, message.str);
    }
  }
}
