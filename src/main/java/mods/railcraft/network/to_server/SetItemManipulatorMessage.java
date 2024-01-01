package mods.railcraft.network.to_server;

import mods.railcraft.Railcraft;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetItemManipulatorMessage(BlockPos blockPos,
                                        ManipulatorBlockEntity.RedstoneMode redstoneMode,
                                        ManipulatorBlockEntity.TransferMode transferMode) implements
    CustomPacketPayload {

  public static final ResourceLocation ID = Railcraft.rl("set_item_manipulator");

  public static SetItemManipulatorMessage decode(FriendlyByteBuf buf) {
    return new SetItemManipulatorMessage(buf.readBlockPos(),
        buf.readEnum(ManipulatorBlockEntity.RedstoneMode.class),
        buf.readEnum(ManipulatorBlockEntity.TransferMode.class));
  }

  @Override
  public void write(FriendlyByteBuf buf) {
    buf.writeBlockPos(this.blockPos);
    buf.writeEnum(this.redstoneMode);
    buf.writeEnum(this.transferMode);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  public void handle(PlayPayloadContext context) {
    context.level()
        .flatMap(level ->
            LevelUtil.getBlockEntity(level, this.blockPos, ItemManipulatorBlockEntity.class))
        .ifPresent(manipulator -> {
          manipulator.setRedstoneMode(this.redstoneMode);
          manipulator.setTransferMode(this.transferMode);
          manipulator.setChanged();
        });
  }
}
