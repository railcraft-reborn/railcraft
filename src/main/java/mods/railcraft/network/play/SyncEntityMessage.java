package mods.railcraft.network.play;

import java.util.function.Supplier;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.NetworkSerializable;
import mods.railcraft.network.NetworkUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class SyncEntityMessage {

  private int entityId;
  private FriendlyByteBuf data;

  public <T extends Entity & NetworkSerializable> SyncEntityMessage(T entity) {
    this(entity.getId(), new FriendlyByteBuf(Unpooled.buffer()));
    entity.writeToBuf(data);
  }

  private SyncEntityMessage(int entityId, FriendlyByteBuf data) {
    this.entityId = entityId;
    this.data = data;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeVarInt(this.entityId);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static SyncEntityMessage decode(FriendlyByteBuf in) {
    return new SyncEntityMessage(in.readVarInt(), new FriendlyByteBuf(in.readBytes(in.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), this.entityId, NetworkSerializable.class).readFromBuf(this.data);
    return true;
  }
}
