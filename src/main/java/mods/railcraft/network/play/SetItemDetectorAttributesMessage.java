package mods.railcraft.network.play;

import java.util.function.Supplier;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

public record SetItemDetectorAttributesMessage(
    BlockPos blockPos,
    ItemDetectorBlockEntity.PrimaryMode primaryMode,
    ItemDetectorBlockEntity.FilterMode filterMode) {

  public void encode(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.primaryMode);
    out.writeEnum(this.filterMode);
  }

  public static SetItemDetectorAttributesMessage decode(FriendlyByteBuf in) {
    return new SetItemDetectorAttributesMessage(in.readBlockPos(),
        in.readEnum(ItemDetectorBlockEntity.PrimaryMode.class),
        in.readEnum(ItemDetectorBlockEntity.FilterMode.class));
  }

  public boolean handle(Supplier<NetworkEvent.Context> context) {
    var level = context.get().getSender().level();
    level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.ITEM_DETECTOR.get())
        .ifPresent(itemDetector -> {
          itemDetector.setPrimaryMode(this.primaryMode);
          itemDetector.setFilterMode(this.filterMode);
          itemDetector.setChanged();
        });
    return true;
  }
}
