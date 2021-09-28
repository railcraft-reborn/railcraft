package mods.railcraft.network.play;

import java.util.function.Supplier;
import io.netty.buffer.Unpooled;
import mods.railcraft.api.core.Syncable;
import mods.railcraft.network.NetworkUtil;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncEntityMessage {

  private int entityId;
  private PacketBuffer data;

  public <T extends Entity & Syncable> SyncEntityMessage(T entity) {
    this(entity.getId(), new PacketBuffer(Unpooled.buffer()));
    entity.writeSyncData(data);
  }

  private SyncEntityMessage(int entityId, PacketBuffer data) {
    this.entityId = entityId;
    this.data = data;
  }

  public void encode(PacketBuffer out) {
    out.writeVarInt(this.entityId);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static SyncEntityMessage decode(PacketBuffer in) {
    return new SyncEntityMessage(in.readVarInt(), new PacketBuffer(in.readBytes(in.readVarInt())));
  }

  public void handle(Supplier<NetworkEvent.Context> ctx) {
    NetworkUtil.getEntity(ctx.get(), this.entityId, Syncable.class).readSyncData(data);
  }
}
