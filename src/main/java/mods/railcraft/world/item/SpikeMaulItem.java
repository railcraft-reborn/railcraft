package mods.railcraft.world.item;

import com.google.common.collect.Lists;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

import javax.annotation.Nullable;

import mods.railcraft.advancements.criterion.RailcraftAdvancementTriggers;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.api.item.SpikeMaulTarget;
import mods.railcraft.api.track.TrackType;
import mods.railcraft.api.track.TypedTrack;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.TrackShapeHelper;
import mods.railcraft.world.level.block.RailcraftBlocks;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
import mods.railcraft.world.level.block.track.outfitted.OutfittedTrackBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.state.properties.RailShape;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class SpikeMaulItem extends ToolItem {

  public SpikeMaulItem(float attackDamage, float attackSpeed, IItemTier tier,
      Properties properties) {
    super(attackDamage, attackSpeed, tier, Collections.emptySet(), properties);
  }

  @Override
  public ActionResultType useOn(ItemUseContext context) {
    World level = context.getLevel();
    BlockPos blockPos = context.getClickedPos();

    if (SpikeMaulTarget.spikeMaulTargets.isEmpty()) {
      return ActionResultType.PASS;
    }

    BlockState existingBlockState = level.getBlockState(blockPos);
    TileEntity existingBlockEntity = null;
    if (existingBlockState.getBlock().hasTileEntity(existingBlockState)) {
      existingBlockEntity = level.getBlockEntity(blockPos);
    }

    if (!AbstractRailBlock.isRail(existingBlockState)) {
      return ActionResultType.PASS;
    }

    RailShape railShape = TrackBlock.getRailShapeRaw(existingBlockState);
    if (TrackShapeHelper.isAscending(railShape)) {
      return ActionResultType.PASS;
    }

    PlayerEntity player = context.getPlayer();
    List<SpikeMaulTarget> list = SpikeMaulTarget.spikeMaulTargets;
    if (player.isCrouching()) {
      list = Lists.reverse(list);
    }
    Deque<SpikeMaulTarget> targets = new ArrayDeque<>(list);
    SpikeMaulTarget first = targets.getFirst();
    SpikeMaulTarget found = null;
    SpikeMaulTarget each;
    do {
      each = targets.removeFirst();
      if (each.matches(existingBlockState, level, blockPos)) {
        found = targets.isEmpty() ? first : targets.getFirst();
        break;
      }
    } while (!targets.isEmpty());

    if (found == null) {
      return ActionResultType.PASS;
    }

    if (level.isClientSide()) {
      return ActionResultType.SUCCESS;
    }

    LevelUtil.setAir(level, blockPos);
    Charge.distribution.network(level).removeNode(blockPos);
    if (!found.use(context)) {
      level.setBlockAndUpdate(blockPos, existingBlockState);

      if (existingBlockEntity != null) {
        existingBlockEntity.clearRemoved();
        level.setBlockEntity(blockPos, existingBlockEntity);
      }
      return ActionResultType.FAIL;
    }

    ItemStack heldStack = player.getItemInHand(context.getHand());
    BlockState newBlockState = level.getBlockState(blockPos);
    SoundType soundtype = newBlockState.getSoundType(level, blockPos, player);
    level.playSound(player, blockPos,
        newBlockState.getSoundType(level, blockPos, player).getPlaceSound(),
        SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

    RailcraftAdvancementTriggers.getInstance().onSpikeMaulUsageSuccess(
        (ServerPlayerEntity) player, level, blockPos, heldStack);
    heldStack.shrink(1);
    return ActionResultType.SUCCESS;
  }

  @Override
  public boolean doesSneakBypassUse(ItemStack itemStack, IWorldReader level, BlockPos blockPos,
      PlayerEntity player) {
    return true;
  }

  @Override
  public boolean canDisableShield(ItemStack itemStack, ItemStack shieldStack, LivingEntity entity,
      LivingEntity attacker) {
    return true;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> lines,
      ITooltipFlag advanced) {
    super.appendHoverText(stack, world, lines, advanced);
    lines.add(new TranslationTextComponent("spike_maul.description"));
  }

  static {
    SpikeMaulTarget.spikeMaulTargets.add(new FlexTarget());
    SpikeMaulTarget.spikeMaulTargets
        .add(new SpikeMaulTarget.Simple(RailcraftBlocks.TURNOUT_TRACK));
    SpikeMaulTarget.spikeMaulTargets
        .add(new SpikeMaulTarget.Simple(RailcraftBlocks.WYE_TRACK));
    // ISpikeMaulTarget.spikeMaulTargets
    // .add(new ISpikeMaulTarget.TrackKitTarget(TrackKits.JUNCTION::getTrackKit));
  }

  private static class FlexTarget implements SpikeMaulTarget {

    @Override
    public boolean matches(BlockState blockState, World level, BlockPos blockPos) {
      return (blockState.getBlock() instanceof TrackBlock
          && !(blockState.getBlock() instanceof OutfittedTrackBlock))
          || blockState.is(Blocks.RAIL);
    }

    @Override
    public boolean use(ItemUseContext context) {
      World level = context.getLevel();
      BlockPos blockPos = context.getClickedPos();
      Block block = level.getBlockState(blockPos).getBlock();
      TrackType trackType = TrackTypes.IRON.get();
      if (block instanceof TypedTrack) {
        trackType = ((TypedTrack) block).getTrackType();
      }
      return level.setBlockAndUpdate(blockPos,
          trackType.getBaseBlock().getStateForPlacement(new BlockItemUseContext(context)));
    }
  }
}
