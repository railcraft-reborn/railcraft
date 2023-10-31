package mods.railcraft.network.play;

import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.manipulator.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.manipulator.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetItemManipulatorAttributesMessage(BlockPos blockPos,
    ManipulatorBlockEntity.RedstoneMode redstoneMode,
    ManipulatorBlockEntity.TransferMode transferMode) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.redstoneMode);
    out.writeEnum(this.transferMode);
  }

  public static SetItemManipulatorAttributesMessage decode(FriendlyByteBuf in) {
    return new SetItemManipulatorAttributesMessage(in.readBlockPos(),
        in.readEnum(ManipulatorBlockEntity.RedstoneMode.class),
        in.readEnum(ManipulatorBlockEntity.TransferMode.class));
  }

  public boolean handle(NetworkEvent.Context context) {
    var level = context.getSender().level();
    LevelUtil.getBlockEntity(level, this.blockPos, ItemManipulatorBlockEntity.class)
        .ifPresent(manipulator -> {
          manipulator.setRedstoneMode(this.redstoneMode);
          manipulator.setTransferMode(this.transferMode);
          manipulator.setChanged();
        });
    return true;
  }
}
