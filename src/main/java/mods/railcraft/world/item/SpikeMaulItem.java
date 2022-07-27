package mods.railcraft.world.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableMultimap.Builder;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.item.SpikeMaulTarget;
import mods.railcraft.api.track.RailShapeUtil;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
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

import javax.annotation.Nullable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;

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
    var level = context.getLevel();
    var blockPos = context.getClickedPos();

    var existingBlockState = level.getBlockState(blockPos);

    if (!BaseRailBlock.isRail(existingBlockState)) {
      return InteractionResult.PASS;
    }

    var railShape = TrackBlock.getRailShapeRaw(existingBlockState);
    if (RailShapeUtil.isAscending(railShape)) {
      return InteractionResult.PASS;
    }

    List<? extends Supplier<? extends Block>> variants;
    if (existingBlockState.getBlock() instanceof SpikeMaulTarget target) {
      variants = target.getSpikeMaulVariants();
    } else if (existingBlockState.is(Blocks.RAIL)) {
      variants = TrackTypes.IRON.get().getSpikeMaulVariants();
    } else {
      return InteractionResult.PASS;
    }

    if (variants.isEmpty()) {
      return InteractionResult.PASS;
    }

    var player = context.getPlayer();
    if (player.isCrouching()) {
      variants = Lists.reverse(variants);
    }
    Deque<? extends Supplier<? extends Block>> targets = new ArrayDeque<>(variants);
    var first = targets.getFirst();
    Supplier<? extends Block> found = null;
    Supplier<? extends Block> each;
    do {
      each = targets.removeFirst();
      if (existingBlockState.is(each.get())) {
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

    if (!level.setBlockAndUpdate(blockPos,
        found.get().getStateForPlacement(new BlockPlaceContext(context)))) {
      level.setBlockAndUpdate(blockPos, existingBlockState);
      return InteractionResult.FAIL;
    }

    var heldStack = player.getItemInHand(context.getHand());
    var newBlockState = level.getBlockState(blockPos);
    var soundtype = newBlockState.getSoundType(level, blockPos, player);
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
    lines.add(Component.translatable("spike_maul.description"));
  }
}
