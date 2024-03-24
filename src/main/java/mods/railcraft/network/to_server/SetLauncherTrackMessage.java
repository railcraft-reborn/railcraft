package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetLauncherTrackMessage(
    BlockPos blockPos, byte force) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_launcher_track");

  public static SetLauncherTrackMessage read(FriendlyByteBuf buf) {
    return new SetLauncherTrackMessage(buf.readBlockPos(), buf.readByte());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeByte(this.force);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level()
        .flatMap(level ->
            level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.LAUNCHER_TRACK.get()))
        .ifPresent(track -> {
          track.setLaunchForce(this.force);
          track.setChanged();
        });
  }
}
