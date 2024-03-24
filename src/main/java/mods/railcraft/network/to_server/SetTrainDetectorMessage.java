package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetTrainDetectorMessage(
    BlockPos blockPos, int trainSize) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_train_detector");

  public static SetTrainDetectorMessage read(FriendlyByteBuf in) {
    return new SetTrainDetectorMessage(in.readBlockPos(), in.readVarInt());
  }

  @Override
  public void write(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeVarInt(this.trainSize);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level().ifPresent(level -> {
      level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.TRAIN_DETECTOR.get())
          .ifPresent(trainDetector -> {
            trainDetector.setTrainSize(this.trainSize);
            trainDetector.setChanged();
          });
    });
  }
}
