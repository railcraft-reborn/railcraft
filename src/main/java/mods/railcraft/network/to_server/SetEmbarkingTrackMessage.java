package mods.railcraft.network.to_server;

import mods.railcraft.Railcraft;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetEmbarkingTrackMessage(BlockPos blockPos, int radius) implements
    CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("set_embarking_track");

  public static SetEmbarkingTrackMessage read(FriendlyByteBuf buf) {
    return new SetEmbarkingTrackMessage(buf.readBlockPos(), buf.readVarInt());
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeVarInt(this.radius);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.level().ifPresent(level -> {
      var blockState = level.getBlockState(this.blockPos);
      if (blockState.getBlock() instanceof EmbarkingTrackBlock) {
        level.setBlockAndUpdate(this.blockPos,
            EmbarkingTrackBlock.setRadius(blockState, this.radius));
      }
    });
  }
}
