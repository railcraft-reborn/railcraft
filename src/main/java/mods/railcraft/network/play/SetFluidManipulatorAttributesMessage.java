package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;

public class SetFluidManipulatorAttributesMessage {

  private final BlockPos blockPos;
  private final ManipulatorBlockEntity.RedstoneMode redstoneMode;

  public SetFluidManipulatorAttributesMessage(BlockPos blockPos,
      ManipulatorBlockEntity.RedstoneMode redstoneMode) {
    this.blockPos = blockPos;
    this.redstoneMode = redstoneMode;
  }

  public void encode(PacketBuffer out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.redstoneMode);
  }

  public static SetFluidManipulatorAttributesMessage decode(PacketBuffer in) {
    return new SetFluidManipulatorAttributesMessage(in.readBlockPos(),
        in.readEnum(ManipulatorBlockEntity.RedstoneMode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    ServerWorld level = context.get().getSender().getLevel();
    LevelUtil.getBlockEntity(level, this.blockPos, FluidManipulatorBlockEntity.class)
        .ifPresent(manipulator -> manipulator.setRedstoneMode(this.redstoneMode));
    return true;
  }
}
