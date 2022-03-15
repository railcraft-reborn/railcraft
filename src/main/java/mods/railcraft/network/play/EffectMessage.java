package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.network.PacketDispatcher;
import mods.railcraft.util.RemoteEffectType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;

public record EffectMessage(RemoteEffectType effect, FriendlyByteBuf data) {

  public void send(Level level, BlockPos pos) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(level.dimension(), pos, 80));
  }

  public void send(Level level, Vec3 vec) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(level.dimension(), vec, 80));
  }

  public void send(Level level, double x, double y, double z) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(level.dimension(), x, y, z, 80));
  }

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(this.effect);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static EffectMessage decode(FriendlyByteBuf in) {
    return new EffectMessage(in.readEnum(RemoteEffectType.class),
        new FriendlyByteBuf(in.readBytes(in.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    this.effect.handle(ClientEffects.INSTANCE, this.data);
    return true;
  }
}
