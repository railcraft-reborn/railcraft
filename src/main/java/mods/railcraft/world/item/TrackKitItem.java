package mods.railcraft.world.item;

import java.util.Map;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableMap;
import mods.railcraft.Translations.Tips;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TrackUtil;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.RegistryObject;

public class TrackKitItem extends Item {

  private final Map<ResourceLocation, Supplier<? extends BaseRailBlock>> outfittedBlocks;
  private final boolean allowedOnSlopes;

  public TrackKitItem(Properties properties) {
    super(properties);
    this.outfittedBlocks = properties.outfittedBlocks.build();
    this.allowedOnSlopes = properties.allowedOnSlopes;
  }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    var player = context.getPlayer();
    var level = context.getLevel();
    var hand = context.getHand();
    var itemStack = player.getItemInHand(hand);
    var blockPos = context.getClickedPos();

    var oldState = level.getBlockState(blockPos);
    if (!BaseRailBlock.isRail(oldState)) {
      return InteractionResult.PASS;
    }

    TrackType trackType;
    if (oldState.getBlock() instanceof TrackBlock trackBlock) {
      trackType = trackBlock.getTrackType();
    } else if (oldState.getBlock() == Blocks.RAIL) {
      trackType = TrackTypes.IRON.get();
    } else {
      return InteractionResult.PASS;
    }

    var shape = TrackUtil.getRailShapeRaw(level, blockPos);

    if (!RailShapeUtil.isStraight(shape)) {
      player.displayClientMessage(Component.translatable(Tips.TRACK_KIT_CORNERS_UNSUPPORTED)
              .withStyle(ChatFormatting.RED), true);
      return InteractionResult.PASS;
    }

    if (shape.isAscending() && !this.allowedOnSlopes) {
      player.displayClientMessage(Component.translatable(Tips.TRACK_KIT_SLOPES_UNSUPPORTED)
          .withStyle(ChatFormatting.RED), true);
      return InteractionResult.PASS;
    }

    var outfittedBlock = this.outfittedBlocks
        .getOrDefault(TrackTypes.REGISTRY.get().getKey(trackType), () -> null).get();
    if (outfittedBlock == null) {
      player.displayClientMessage(Component.translatable(Tips.TRACK_KIT_INVALID_TRACK_TYPE)
          .withStyle(ChatFormatting.RED), true);
      return InteractionResult.PASS;
    }

    var outfittedBlockState = outfittedBlock.getStateForPlacement(new BlockPlaceContext(context));
    if (level.setBlockAndUpdate(blockPos, outfittedBlockState)) {
      var soundType = outfittedBlock.getSoundType(outfittedBlockState, level, blockPos, player);
      level.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
          soundType.getVolume(), soundType.getPitch());
      if (!level.isClientSide()) {
        RailcraftCriteriaTriggers.TRACK_KIT_USE.trigger(
            (ServerPlayer) player, (ServerLevel) level, blockPos, itemStack);
      }

      if (!player.getAbilities().instabuild) {
        itemStack.shrink(1);
      }
      return InteractionResult.SUCCESS;
    }

    return InteractionResult.PASS;
  }

  public static class Properties extends Item.Properties {

    private final ImmutableMap.Builder<ResourceLocation, Supplier<? extends BaseRailBlock>> outfittedBlocks =
        ImmutableMap.builder();
    private boolean allowedOnSlopes;

    public Properties addOutfittedBlock(
        RegistryObject<? extends TrackType> trackType,
        Supplier<? extends BaseRailBlock> block) {
      return this.addOutfittedBlock(trackType.getId(), block);
    }

    public Properties addOutfittedBlock(ResourceLocation trackTypeId,
        Supplier<? extends BaseRailBlock> block) {
      this.outfittedBlocks.put(trackTypeId, block);
      return this;
    }

    public Properties setAllowedOnSlopes(boolean allowedOnSlopes) {
      this.allowedOnSlopes = allowedOnSlopes;
      return this;
    }
  }
}
