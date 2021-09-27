package mods.railcraft.world.item;

import java.util.function.Supplier;
import mods.railcraft.advancements.criterion.RailcraftAdvancementTriggers;
import mods.railcraft.api.tracks.TrackKit;
import mods.railcraft.api.tracks.TrackType;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.state.properties.RailShape;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class TrackKitItem extends Item {

  private final Supplier<? extends TrackKit> trackKit;

  public TrackKitItem(Supplier<? extends TrackKit> trackKit, Properties properties) {
    super(properties);
    this.trackKit = trackKit;
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    World level = context.getLevel();
    ItemStack itemStack = player.getItemInHand(context.getHand());
    BlockPos blockPos = context.getClickedPos();
    player.swing(context.getHand());

    if (level.isClientSide())
      return ActionResultType.PASS;

    BlockState oldState = level.getBlockState(blockPos);
    if (!AbstractRailBlock.isRail(oldState)) {
      return ActionResultType.PASS;
    }

    TrackType trackType = null;
    if (oldState.getBlock() instanceof TrackBlock) {
      trackType = ((TrackBlock) oldState.getBlock()).getTrackType();
    } else if (oldState.getBlock() == Blocks.RAIL) {
      trackType = TrackTypes.IRON.get();
    }

    if (trackType != null) {
      RailShape shape = TrackTools.getTrackDirectionRaw(level, blockPos);
      if (TrackShapeHelper.isStraight(shape)) {
        if (!shape.isAscending() || trackKit.get().isAllowedOnSlopes()) {
          AbstractRailBlock outfittedBlock = trackType.getOutfittedBlock(this.trackKit.get());
          if (outfittedBlock == null) {
            player.sendMessage(
                new TranslationTextComponent("gui.railcraft.track_kit.item.invalid.track_type"),
                Util.NIL_UUID);
            return ActionResultType.PASS;
          }

          BlockState outfittedBlockState = outfittedBlock.defaultBlockState();
          if (level.setBlockAndUpdate(blockPos, outfittedBlockState)) {
            SoundType soundType =
                outfittedBlock.getSoundType(outfittedBlockState, level, blockPos, player);
            level.playSound(player, blockPos, soundType.getPlaceSound(), SoundCategory.BLOCKS,
                soundType.getVolume(), soundType.getPitch());
            RailcraftAdvancementTriggers.getInstance().onTrackKitUse((ServerPlayerEntity) player,
                level, blockPos, itemStack);
            if (!player.abilities.instabuild) {
              itemStack.shrink(1);
            }
            return ActionResultType.SUCCESS;
          }
        } else {
          player.sendMessage(
              new TranslationTextComponent("gui.railcraft.track_kit.item.invalid.slope"),
              Util.NIL_UUID);
        }
      } else {
        player.sendMessage(
            new TranslationTextComponent("gui.railcraft.track_kit.item.invalid.curve"),
            Util.NIL_UUID);
      }
    } else {
      player.sendMessage(
          new TranslationTextComponent("gui.railcraft.track_kit.item.invalid.track"),
          Util.NIL_UUID);
    }
    return ActionResultType.PASS;
  }
}
