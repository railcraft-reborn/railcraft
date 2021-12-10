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

public class PacketEffect {

  private RemoteEffectType effect;
  private FriendlyByteBuf data;

  public PacketEffect(RemoteEffectType effect, FriendlyByteBuf data) {
    this.effect = effect;
    this.data = data;
  }

  public void sendPacket(Level world, BlockPos pos) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(world.dimension(), pos, 80));
  }

  public void sendPacket(Level world, Vec3 vec) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(world.dimension(), vec, 80));
  }

  public void sendPacket(Level world, double x, double y, double z) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(world.dimension(), x, y, z, 80));
  }

  public void encode(FriendlyByteBuf out) {
    out.writeEnum(effect);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static PacketEffect decode(FriendlyByteBuf in) {
    return new PacketEffect(in.readEnum(RemoteEffectType.class),
        new FriendlyByteBuf(in.readBytes(in.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    this.effect.handle(ClientEffects.INSTANCE, this.data);
    return true;
  }
}
