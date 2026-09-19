package mods.railcraft.network.to_server;

import mods.railcraft.api.core.RailcraftConstants;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import mods.railcraft.world.level.block.entity.detector.ItemDetectorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SetItemDetectorMessage(
    BlockPos blockPos,
    ItemDetectorBlockEntity.PrimaryMode primaryMode,
    ItemDetectorBlockEntity.FilterMode filterMode) implements CustomPacketPayload {

  public static final Type<SetItemDetectorMessage> TYPE =
      new Type<>(RailcraftConstants.id("set_item_detector"));

  public static final StreamCodec<FriendlyByteBuf, SetItemDetectorMessage> STREAM_CODEC =
      StreamCodec.composite(
          BlockPos.STREAM_CODEC, SetItemDetectorMessage::blockPos,
          NeoForgeStreamCodecs.enumCodec(ItemDetectorBlockEntity.PrimaryMode.class), SetItemDetectorMessage::primaryMode,
          NeoForgeStreamCodecs.enumCodec(ItemDetectorBlockEntity.FilterMode.class), SetItemDetectorMessage::filterMode,
          SetItemDetectorMessage::new);

  @Override
  public Type<? extends CustomPacketPayload> type() {
    return TYPE;
  }

  public static void handle(SetItemDetectorMessage message, IPayloadContext context) {
    context.player().level()
        .getBlockEntity(message.blockPos, RailcraftBlockEntityTypes.ITEM_DETECTOR.get())
        .ifPresent(itemDetector -> {
          itemDetector.setPrimaryMode(message.primaryMode);
          itemDetector.setFilterMode(message.filterMode);
          itemDetector.setChanged();
        });
  }
}
