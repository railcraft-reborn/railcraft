package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.ItemManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetItemManipulatorAttributesMessage {

  private final BlockPos blockPos;
  private final ManipulatorBlockEntity.RedstoneMode redstoneMode;
  private final ManipulatorBlockEntity.TransferMode transferMode;

  public SetItemManipulatorAttributesMessage(BlockPos blockPos,
      ManipulatorBlockEntity.RedstoneMode redstoneMode,
      ManipulatorBlockEntity.TransferMode transferMode) {
    this.blockPos = blockPos;
    this.redstoneMode = redstoneMode;
    this.transferMode = transferMode;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.redstoneMode);
    out.writeEnum(this.transferMode);
  }

  public static SetItemManipulatorAttributesMessage decode(PacketBuffer in) {
    return new SetItemManipulatorAttributesMessage(in.readBlockPos(),
        in.readEnum(ManipulatorBlockEntity.RedstoneMode.class),
        in.readEnum(ManipulatorBlockEntity.TransferMode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ServerWorld level = context.get().getSender().getLevel();
    LevelUtil.getBlockEntity(level, this.blockPos, ItemManipulatorBlockEntity.class)
        .ifPresent(manipulator -> {
          manipulator.setRedstoneMode(this.redstoneMode);
          manipulator.setTransferMode(this.transferMode);
        });
    return true;
  }
}
