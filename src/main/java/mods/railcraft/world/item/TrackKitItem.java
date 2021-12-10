package mods.railcraft.world.item;

import java.util.Map;
import java.util.function.Supplier;
import com.google.common.collect.ImmutableMap;
import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.util.TrackTools;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
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
    Player player = context.getPlayer();
    Level level = context.getLevel();
    ItemStack itemStack = player.getItemInHand(context.getHand());
    BlockPos blockPos = context.getClickedPos();

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    BlockState oldState = level.getBlockState(blockPos);
    if (!BaseRailBlock.isRail(oldState)) {
      return InteractionResult.PASS;
    }

    TrackType trackType;
    if (oldState.getBlock() instanceof TrackBlock) {
      trackType = ((TrackBlock) oldState.getBlock()).getTrackType();
    } else if (oldState.getBlock() == Blocks.RAIL) {
      trackType = TrackTypes.IRON.get();
    } else {
      return InteractionResult.PASS;
    }

    RailShape shape = TrackTools.getRailShapeRaw(level, blockPos);

    if (!TrackShapeHelper.isStraight(shape)) {
      player.displayClientMessage(new TranslatableComponent("track_kit.corners_unsupported"),
          true);
      return InteractionResult.PASS;
    }

    if (shape.isAscending() && !this.allowedOnSlopes) {
      player.displayClientMessage(new TranslatableComponent("track_kit.slopes_unsupported"),
          true);
      return InteractionResult.PASS;
    }

    BaseRailBlock outfittedBlock =
        this.outfittedBlocks.getOrDefault(trackType.getRegistryName(), () -> null).get();
    if (outfittedBlock == null) {
      player.displayClientMessage(
          new TranslatableComponent("track_kit.invalid_track_type"), true);
      return InteractionResult.PASS;
    }

    BlockState outfittedBlockState =
        outfittedBlock.getStateForPlacement(new BlockPlaceContext(context));
    if (level.setBlockAndUpdate(blockPos, outfittedBlockState)) {
      SoundType soundType =
          outfittedBlock.getSoundType(outfittedBlockState, level, blockPos, player);
      level.playSound(player, blockPos, soundType.getPlaceSound(), SoundSource.BLOCKS,
          soundType.getVolume(), soundType.getPitch());

      RailcraftCriteriaTriggers.TRACK_KIT_USE.trigger(
          (ServerPlayer) player, (ServerLevel) level, blockPos, itemStack);

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
