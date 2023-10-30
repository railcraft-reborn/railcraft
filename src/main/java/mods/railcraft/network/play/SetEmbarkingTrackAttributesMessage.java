package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetEmbarkingTrackAttributesMessage(BlockPos blockPos, int radius) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.radius);
  }

  public static SetEmbarkingTrackAttributesMessage decode(FriendlyByteBuf in) {
    return new SetEmbarkingTrackAttributesMessage(in.readBlockPos(), in.readVarInt());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().level();
    var blockState = level.getBlockState(this.blockPos);
    if (blockState.getBlock() instanceof EmbarkingTrackBlock) {
      level.setBlockAndUpdate(this.blockPos,
          EmbarkingTrackBlock.setRadius(blockState, this.radius));
    }
    return true;
  }
}
