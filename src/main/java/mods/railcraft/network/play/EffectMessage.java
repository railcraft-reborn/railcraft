package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.client.ClientEffects;
import mods.railcraft.network.NetworkChannel;
import mods.railcraft.network.NetworkUtil;
import mods.railcraft.util.RemoteEffectType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

public record EffectMessage(RemoteEffectType effect, FriendlyByteBuf data) {

  private static final int DISPATCH_RADIUS = 80;

  public void send(Level level, BlockPos pos) {
    NetworkChannel.GAME.sendToAllAround(this,
        NetworkUtil.targetPoint(level.dimension(), pos, DISPATCH_RADIUS));
  }

  public void send(Level level, Vec3 vec) {
    NetworkChannel.GAME.sendToAllAround(this,
        NetworkUtil.targetPoint(level.dimension(), vec, DISPATCH_RADIUS));
  }

  public void send(Level level, double x, double y, double z) {
    NetworkChannel.GAME.sendToAllAround(this,
        new PacketDistributor.TargetPoint(x, y, z, DISPATCH_RADIUS, level.dimension()));
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
