package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.TankDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetTankDetectorAttributesMessage(
    BlockPos blockPos,
    TankDetectorBlockEntity.Mode mode) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.mode);
  }

  public static SetTankDetectorAttributesMessage decode(FriendlyByteBuf in) {
    return new SetTankDetectorAttributesMessage(in.readBlockPos(),
        in.readEnum(TankDetectorBlockEntity.Mode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().level();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.TANK_DETECTOR.get())
        .ifPresent(blockEntity -> {
          blockEntity.setMode(mode);
          blockEntity.setChanged();
        });
    return true;
  }
}
