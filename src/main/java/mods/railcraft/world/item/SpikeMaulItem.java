package mods.railcraft.world.item;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import com.google.common.collect.Lists;
import mods.railcraft.Translations.Tips;
import mods.railcraft.advancements.RailcraftCriteriaTriggers;
import mods.railcraft.api.item.SpikeMaulTarget;
import mods.railcraft.world.level.block.track.TrackBlock;
import mods.railcraft.world.level.block.track.TrackTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class SpikeMaulItem extends Item {

  public SpikeMaulItem(float attackDamage, float attackSpeed, ToolMaterial material, Properties properties) {
    super(properties
        .durability(material.durability())
        .attributes(
            ItemAttributeModifiers.builder()
                .add(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_ID,
                    attackDamage + material.attackDamageBonus(), AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .add(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_ID,
                    attackSpeed, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                .build()
    ));
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
    if (railShape.isSlope()) {
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

    if (!level.setBlockAndUpdate(blockPos,
        found.get().getStateForPlacement(new BlockPlaceContext(context)))) {
      level.setBlockAndUpdate(blockPos, existingBlockState);
      return InteractionResult.FAIL;
    }

    var hand = context.getHand();
    var heldStack = player.getItemInHand(hand);
    var newBlockState = level.getBlockState(blockPos);
    var soundtype = newBlockState.getSoundType(level, blockPos, player);
    level.playSound(player, blockPos, soundtype.getPlaceSound(), SoundSource.BLOCKS,
        (soundtype.getVolume() + 1) / 2, soundtype.getPitch() * 0.8F);

    if (level instanceof ServerLevel serverLevel) {
      RailcraftCriteriaTriggers.SPIKE_MAUL_USE.value().trigger(
          (ServerPlayer) player, heldStack, serverLevel, blockPos);

      heldStack.hurtAndBreak(1, serverLevel, player,
          item -> player.onEquippedItemBroken(item, LivingEntity.getSlotForHand(hand)));
    }
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
  public void appendHoverText(ItemStack stack, TooltipContext context,
      List<Component> components, TooltipFlag isAdvanced) {
    components.add(Component.translatable(Tips.SPIKE_MAUL).withStyle(ChatFormatting.GRAY));
  }
}
