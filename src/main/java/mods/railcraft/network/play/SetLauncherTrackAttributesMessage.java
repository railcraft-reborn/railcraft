package mods.railcraft.network.play;

import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.neoforge.network.NetworkEvent;

public record SetLauncherTrackAttributesMessage(BlockPos blockPos, byte force) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeByte(this.force);
  }

  public static SetLauncherTrackAttributesMessage decode(FriendlyByteBuf in) {
    return new SetLauncherTrackAttributesMessage(in.readBlockPos(), in.readByte());
  }

  public boolean handle(NetworkEvent.Context context) {
    var level = context.getSender().level();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.LAUNCHER_TRACK.get())
        .ifPresent(track -> {
          track.setLaunchForce(this.force);
          track.setChanged();
        });
    return true;
  }
}
