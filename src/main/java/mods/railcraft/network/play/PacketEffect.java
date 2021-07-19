package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.network.PacketDispatcher;
import mods.railcraft.util.RemoteEffectType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

public class PacketEffect {

  private RemoteEffectType effect;
  private PacketBuffer data;

  public PacketEffect(RemoteEffectType effect, PacketBuffer data) {
    this.effect = effect;
    this.data = data;
  }

  public void sendPacket(World world, BlockPos pos) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(world.dimension(), pos, 80));
  }

  public void sendPacket(World world, Vector3d vec) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(world.dimension(), vec, 80));
  }

  public void sendPacket(World world, double x, double y, double z) {
    PacketDispatcher.sendToAllAround(this,
        PacketDispatcher.targetPoint(world.dimension(), x, y, z, 80));
  }

  public void encode(PacketBuffer out) {
    out.writeEnum(effect);
    out.writeVarInt(this.data.readableBytes());
    out.writeBytes(this.data);
  }

  public static PacketEffect decode(PacketBuffer in) {
    return new PacketEffect(in.readEnum(RemoteEffectType.class),
        new PacketBuffer(in.readBytes(in.readVarInt())));
  }

  public boolean handle(Supplier<NetworkEvent.Context> ctx) {
    this.effect.handle(ClientEffects.INSTANCE, this.data);
    return true;
  }
}
