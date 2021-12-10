package mods.railcraft.world.item;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import mods.railcraft.advancements.criterion.RailcraftCriteriaTriggers;
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
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;

public class SpikeMaulItem extends TieredItem {

  private final float attackDamage;
  private final Multimap<Attribute, AttributeModifier> defaultModifiers;

  public SpikeMaulItem(float attackDamage, float attackSpeed, Tier tier, Properties properties) {
    super(tier, properties);
    this.attackDamage = attackDamage + tier.getAttackDamageBonus();
    Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
    builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID,
        "Weapon modifier", this.attackDamage, AttributeModifier.Operation.ADDITION));
    builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID,
        "Weapon modifier", attackSpeed, AttributeModifier.Operation.ADDITION));
    this.defaultModifiers = builder.build();
  }
  
  @SuppressWarnings("deprecation")
  @Override
  public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
    return slot == EquipmentSlot.MAINHAND ? this.defaultModifiers
        : super.getDefaultAttributeModifiers(slot);
 }

  @Override
  public InteractionResult useOn(UseOnContext context) {
    Level level = context.getLevel();
    BlockPos blockPos = context.getClickedPos();

    if (SpikeMaulTarget.spikeMaulTargets.isEmpty()) {
      return InteractionResult.PASS;
    }

    BlockState existingBlockState = level.getBlockState(blockPos);

    if (!BaseRailBlock.isRail(existingBlockState)) {
      return InteractionResult.PASS;
    }

    RailShape railShape = TrackBlock.getRailShapeRaw(existingBlockState);
    if (TrackShapeHelper.isAscending(railShape)) {
      return InteractionResult.PASS;
    }

    Player player = context.getPlayer();
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
      return InteractionResult.PASS;
    }

    if (level.isClientSide()) {
      return InteractionResult.SUCCESS;
    }

    LevelUtil.setAir(level, blockPos);
    Charge.distribution.network(level).removeNode(blockPos);
    if (!found.use(context)) {
      level.setBlockAndUpdate(blockPos, existingBlockState);
      return InteractionResult.FAIL;
    }

    ItemStack heldStack = player.getItemInHand(context.getHand());
    BlockState newBlockState = level.getBlockState(blockPos);
    SoundType soundtype = newBlockState.getSoundType(level, blockPos, player);
    level.playSound(player, blockPos,
        newBlockState.getSoundType(level, blockPos, player).getPlaceSound(),
        SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);

    RailcraftCriteriaTriggers.SPIKE_MAUL_USE.trigger(
        (ServerPlayer) player, heldStack, (ServerLevel) level, blockPos);

    heldStack.shrink(1);
    return InteractionResult.SUCCESS;
  }

  @Override
  public boolean doesSneakBypassUse(ItemStack itemStack, LevelReader level, BlockPos blockPos,
      Player player) {
    return true;
  }

  @Override
  public boolean canDisableShield(ItemStack itemStack, ItemStack shieldStack, LivingEntity entity,
      LivingEntity attacker) {
    return true;
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> lines,
      TooltipFlag advanced) {
    super.appendHoverText(stack, world, lines, advanced);
    lines.add(new TranslatableComponent("spike_maul.description"));
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
    public boolean matches(BlockState blockState, Level level, BlockPos blockPos) {
      return (blockState.getBlock() instanceof TrackBlock
          && !(blockState.getBlock() instanceof OutfittedTrackBlock))
          || blockState.is(Blocks.RAIL);
    }

    @Override
    public boolean use(UseOnContext context) {
      Level level = context.getLevel();
      BlockPos blockPos = context.getClickedPos();
      Block block = level.getBlockState(blockPos).getBlock();
      TrackType trackType = TrackTypes.IRON.get();
      if (block instanceof TypedTrack) {
        trackType = ((TypedTrack) block).getTrackType();
      }
      return level.setBlockAndUpdate(blockPos,
          trackType.getBaseBlock().getStateForPlacement(new BlockPlaceContext(context)));
    }
  }
}
