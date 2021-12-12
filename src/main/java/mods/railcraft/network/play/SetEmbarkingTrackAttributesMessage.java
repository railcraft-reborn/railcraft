package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.track.outfitted.EmbarkingTrackBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public class SetEmbarkingTrackAttributesMessage {

  private final BlockPos blockPos;
  private final int radius;

  public SetEmbarkingTrackAttributesMessage(BlockPos blockPos, int radius) {
    this.blockPos = blockPos;
    this.radius = radius;
  }

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.radius);
  }

  public static SetEmbarkingTrackAttributesMessage decode(FriendlyByteBuf in) {
    return new SetEmbarkingTrackAttributesMessage(in.readBlockPos(), in.readVarInt());
  }

  public void handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().getLevel();
    var blockState = level.getBlockState(this.blockPos);
    if (blockState.getBlock() instanceof EmbarkingTrackBlock) {
      level.setBlockAndUpdate(this.blockPos,
          EmbarkingTrackBlock.setRadius(blockState, this.radius));
    }
  }
}
