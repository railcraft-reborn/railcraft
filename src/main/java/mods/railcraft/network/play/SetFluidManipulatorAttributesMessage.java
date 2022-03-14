package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.level.block.entity.FluidManipulatorBlockEntity;
import mods.railcraft.world.level.block.entity.ManipulatorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetFluidManipulatorAttributesMessage(BlockPos blockPos,
    ManipulatorBlockEntity.RedstoneMode redstoneMode) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.redstoneMode);
  }

  public static SetFluidManipulatorAttributesMessage decode(FriendlyByteBuf in) {
    return new SetFluidManipulatorAttributesMessage(in.readBlockPos(),
        in.readEnum(ManipulatorBlockEntity.RedstoneMode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().getLevel();
    LevelUtil.getBlockEntity(level, this.blockPos, FluidManipulatorBlockEntity.class)
        .ifPresent(manipulator -> manipulator.setRedstoneMode(this.redstoneMode));
    return true;
  }
}
