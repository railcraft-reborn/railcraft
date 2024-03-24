package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetTankDetectorMessage(
    BlockPos blockPos,
    TankDetectorBlockEntity.Mode mode) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_tank_detector");

  public static SetTankDetectorMessage read(FriendlyByteBuf in) {
    return new SetTankDetectorMessage(in.readBlockPos(),
        in.readEnum(TankDetectorBlockEntity.Mode.class));
  }

  @Override
  public void write(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.mode);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level().ifPresent(level -> {
      level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.TANK_DETECTOR.get())
          .ifPresent(blockEntity -> {
            blockEntity.setMode(mode);
            blockEntity.setChanged();
          });
    });
  }
}
