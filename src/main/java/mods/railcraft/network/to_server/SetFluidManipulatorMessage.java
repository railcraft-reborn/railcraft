package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.manipulator.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetFluidManipulatorMessage(
    BlockPos blockPos,
    ManipulatorBlockEntity.RedstoneMode redstoneMode) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_fluid_manipulator");

  public static SetFluidManipulatorMessage read(FriendlyByteBuf buf) {
    return new SetFluidManipulatorMessage(buf.readBlockPos(),
        buf.readEnum(ManipulatorBlockEntity.RedstoneMode.class));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.redstoneMode);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level()
        .flatMap(level -> LevelUtil
            .getBlockEntity(level, this.blockPos, FluidManipulatorBlockEntity.class))
        .ifPresent(manipulator -> {
          manipulator.setRedstoneMode(this.redstoneMode);
          manipulator.setChanged();
        });
  }
}
