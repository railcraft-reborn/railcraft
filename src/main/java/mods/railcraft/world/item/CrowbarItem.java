package mods.railcraft.world.item;

import java.util.Set;
import java.util.function.Consumer;
import mods.railcraft.Translations;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.LeverBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;

public class CrowbarItem extends Item implements Crowbar {

  private static final int BOOST_DAMAGE = 1;
  private final Set<Class<? extends Block>> shiftRotations =
      Set.of(LeverBlock.class, ButtonBlock.class, ChestBlock.class);
  private final Set<Class<? extends Block>> bannedRotations = Set.of(BaseRailBlock.class);

  public CrowbarItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean doesSneakBypassUse(ItemStack stack, LevelReader world, BlockPos pos,
      Player player) {
    return true;
  }

  private boolean isShiftRotation(Class<? extends Block> cls) {
    return this.shiftRotations.stream().anyMatch(shift -> shift.isAssignableFrom(cls));
  }

  private boolean isBannedRotation(Class<? extends Block> cls) {
    return this.bannedRotations.stream().anyMatch(banned -> banned.isAssignableFrom(cls));
  }

  @Override
  public InteractionResult onItemUseFirst(ItemStack itemStack, UseOnContext context) {
    var player = context.getPlayer();
    var hand = context.getHand();
    var stack = player.getItemInHand(hand);
    var level = context.getLevel();
    var pos = context.getClickedPos();
    var blockState = level.getBlockState(pos);

    if (blockState.isAir()) {
      return InteractionResult.PASS;
    }

    if (player.isShiftKeyDown() != this.isShiftRotation(blockState.getBlock().getClass())) {
      return InteractionResult.PASS;
    }

    if (this.isBannedRotation(blockState.getBlock().getClass())) {
      return InteractionResult.PASS;
    }

    if (level instanceof ServerLevel serverLevel) {
      var newBlockState = blockState.rotate(level, pos, Rotation.CLOCKWISE_90);
      if (newBlockState != blockState) {
        level.setBlockAndUpdate(pos, newBlockState);
        player.swing(hand);
        stack.hurtAndBreak(1, serverLevel, player,
            item -> player.onEquippedItemBroken(item, hand.asEquipmentSlot()));
        return InteractionResult.SUCCESS;
      }
    }

    return InteractionResult.PASS;
  }

  @Override
  public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos,
      LivingEntity entityLiving) {
    if (level instanceof ServerLevel serverLevel
        && entityLiving instanceof Player player && !player.isShiftKeyDown()) {
      var destructionEnchantment = level.registryAccess()
          .lookupOrThrow(Registries.ENCHANTMENT)
          .getOrThrow(RailcraftEnchantments.DESTRUCTION);
      int enchantLevel = stack.getEnchantmentLevel(destructionEnchantment) * 2 + 1;
      if (enchantLevel > 1) {
        checkBlock(serverLevel, enchantLevel, pos, player);
      }
    }
    return super.mineBlock(stack, level, state, pos, entityLiving);
  }

  /**
   * Current implementations of this method in child classes do not use the entry argument beside
   * ev. They just raise the damage on the stack.
   */
  @Override
  public void hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    if (attacker instanceof ServerPlayer player) {
      stack.hurtAndBreak(2, player.level(), attacker,
          item -> attacker.onEquippedItemBroken(item, EquipmentSlot.MAINHAND));
    }
  }

  @Override
  public boolean canWhack(Player player, InteractionHand hand, ItemStack crowbar, BlockPos pos) {
    return true;
  }

  @Override
  public void onWhack(ServerPlayer player, InteractionHand hand, ItemStack crowbar, BlockPos pos) {
    crowbar.hurtAndBreak(1, player.level(), player,
        item -> player.onEquippedItemBroken(item, hand.asEquipmentSlot()));
    player.swing(hand);
  }

  @Override
  public boolean canLink(Player player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    return player.isShiftKeyDown();
  }

  @Override
  public void onLink(ServerPlayer player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    crowbar.hurtAndBreak(1, player.level(), player,
        item -> player.onEquippedItemBroken(item, hand.asEquipmentSlot()));
    player.swing(hand);
  }

  @Override
  public boolean canBoost(Player player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    return !player.isShiftKeyDown();
  }

  @Override
  public void onBoost(ServerPlayer player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    crowbar.hurtAndBreak(BOOST_DAMAGE, player.level(), player,
        item -> player.onEquippedItemBroken(item, hand.asEquipmentSlot()));
    player.swing(hand);
  }

  @Override
  public void appendHoverText(ItemStack stack, TooltipContext context,
      TooltipDisplay tooltipDisplay, Consumer<Component> tooltipAdder, TooltipFlag flag) {
    tooltipAdder.accept(Component.translatable(Translations.Tips.CROWBAR_DESC)
        .withStyle(ChatFormatting.ITALIC));
  }

  private static void removeExtraBlocks(ServerLevel level, int enchantmentLevel, BlockPos pos,
      Player player) {
    if (enchantmentLevel > 0) {
      LevelUtil.playerRemoveBlock(level, pos, player);
      checkBlocks(level, enchantmentLevel, pos, player);
    }
  }

  private static void checkBlock(ServerLevel level, int enchantmentLevel, BlockPos pos, Player player) {
    var state = level.getBlockState(pos);
    if (player.hasCorrectToolForDrops(state)) {
      removeExtraBlocks(level, enchantmentLevel - 1, pos, player);
    }
  }

  private static void checkBlocks(ServerLevel level, int enchantmentLevel, BlockPos pos, Player player) {
    // NORTH
    checkBlock(level, enchantmentLevel, pos.offset(0, 0, -1), player);
    checkBlock(level, enchantmentLevel, pos.offset(0, 1, -1), player);
    checkBlock(level, enchantmentLevel, pos.offset(0, -1, -1), player);
    // SOUTH
    checkBlock(level, enchantmentLevel, pos.offset(0, 0, 1), player);
    checkBlock(level, enchantmentLevel, pos.offset(0, 1, 1), player);
    checkBlock(level, enchantmentLevel, pos.offset(0, -1, 1), player);
    // EAST
    checkBlock(level, enchantmentLevel, pos.offset(1, 0, 0), player);
    checkBlock(level, enchantmentLevel, pos.offset(1, 1, 0), player);
    checkBlock(level, enchantmentLevel, pos.offset(1, -1, 0), player);
    // WEST
    checkBlock(level, enchantmentLevel, pos.offset(-1, 0, 0), player);
    checkBlock(level, enchantmentLevel, pos.offset(-1, 1, 0), player);
    checkBlock(level, enchantmentLevel, pos.offset(-1, -1, 0), player);
    // UP_DOWN
    checkBlock(level, enchantmentLevel, pos.above(), player);
    checkBlock(level, enchantmentLevel, pos.below(), player);
  }
}
