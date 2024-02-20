package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetTrainDetectorAttributesMessage(BlockPos blockPos, int trainSize) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.trainSize);
  }

  public static SetTrainDetectorAttributesMessage decode(FriendlyByteBuf in) {
    return new SetTrainDetectorAttributesMessage(in.readBlockPos(), in.readVarInt());
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().level();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.TRAIN_DETECTOR.get())
        .ifPresent(trainDetector -> {
          trainDetector.setTrainSize(this.trainSize);
          trainDetector.setChanged();
        });
    return true;
  }
}
