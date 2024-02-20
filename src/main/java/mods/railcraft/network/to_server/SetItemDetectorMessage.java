package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.network.RailcraftCustomPacketPayload;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public record SetItemDetectorMessage(
    BlockPos blockPos,
    ItemDetectorBlockEntity.PrimaryMode primaryMode,
    ItemDetectorBlockEntity.FilterMode filterMode) implements RailcraftCustomPacketPayload {

  public static final ResourceLocation ID = RailcraftConstants.rl("set_item_detector");

  public static SetItemDetectorMessage read(FriendlyByteBuf in) {
    return new SetItemDetectorMessage(in.readBlockPos(),
        in.readEnum(ItemDetectorBlockEntity.PrimaryMode.class),
        in.readEnum(ItemDetectorBlockEntity.FilterMode.class));
  }

  @Override
  public void write(FriendlyByteBuf out) {
    out.writeBlockPos(this.blockPos);
    out.writeEnum(this.primaryMode);
    out.writeEnum(this.filterMode);
  }

  @Override
  public ResourceLocation id() {
    return ID;
  }

  @Override
  public void handle(PlayPayloadContext context) {
    context.level().ifPresent(level -> {
      level.getBlockEntity(this.blockPos, RailcraftBlockEntityTypes.ITEM_DETECTOR.get())
          .ifPresent(itemDetector -> {
            itemDetector.setPrimaryMode(this.primaryMode);
            itemDetector.setFilterMode(this.filterMode);
            itemDetector.setChanged();
          });
    });
  }
}
