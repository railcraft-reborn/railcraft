package mods.railcraft.world.item;

import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.api.item.Crowbar;
import mods.railcraft.tags.RailcraftTags;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
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

public class CrowbarItem extends DiggerItem implements Crowbar {

  private static final int BOOST_DAMAGE = 1;
  private final Set<Class<? extends Block>> shiftRotations =
      Set.of(LeverBlock.class, ButtonBlock.class, ChestBlock.class);
  private final Set<Class<? extends Block>> bannedRotations = Set.of(BaseRailBlock.class);

  public CrowbarItem(float attackDamage, float attackSpeed, Tier tier, Properties properties) {
    super(attackDamage, attackSpeed, tier, RailcraftTags.Blocks.MINEABLE_WITH_CROWBAR, properties);
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

    if (!level.isClientSide()) {
      var newBlockState = blockState.rotate(level, pos, Rotation.CLOCKWISE_90);
      if (newBlockState != blockState) {
        level.setBlockAndUpdate(pos, newBlockState);
        player.swing(hand);
        stack.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        return InteractionResult.SUCCESS;
      }
    }

    return InteractionResult.PASS;
  }

  @Override
  public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos,
      LivingEntity entityLiving) {
    if (!level.isClientSide()
        && entityLiving instanceof Player player && !player.isShiftKeyDown()) {
      int enchantLevel =
          stack.getEnchantmentLevel(RailcraftEnchantments.DESTRUCTION.get()) * 2 + 1;
      if (enchantLevel > 1) {
        checkBlock(level, enchantLevel, pos, player);
      }
    }
    return super.mineBlock(stack, level, state, pos, entityLiving);
  }

  /**
   * Current implementations of this method in child classes do not use the entry argument beside
   * ev. They just raise the damage on the stack.
   */
  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.hurtAndBreak(2, attacker, __ -> attacker.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    return true;
  }
  // @Override
  // public EnumAction getItemUseAction(ItemStack stack) {
  // return EnumAction.BLOCK;
  // }

  // @Override
  // public int getMaxItemUseDuration(ItemStack par1ItemStack) {
  // return 72000;
  // }

  // @Override
  // public ActionResult<ItemStack> onItemRightClick(ItemStack itemStack, World world, PlayerEntity
  // player, Hand hand) {
  // player.setActiveHand(hand);
  // return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
  // }



  @Override
  public boolean canWhack(Player player, InteractionHand hand, ItemStack crowbar, BlockPos pos) {
    return true;
  }

  @Override
  public void onWhack(Player player, InteractionHand hand, ItemStack crowbar, BlockPos pos) {
    crowbar.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    player.swing(hand);
  }

  @Override
  public boolean canLink(Player player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    return player.isShiftKeyDown();
  }

  @Override
  public void onLink(Player player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    crowbar.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    player.swing(hand);
  }

  @Override
  public boolean canBoost(Player player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    return !player.isShiftKeyDown();
  }

  @Override
  public void onBoost(Player player, InteractionHand hand, ItemStack crowbar,
      AbstractMinecart cart) {
    crowbar.hurtAndBreak(BOOST_DAMAGE, player,
        __ -> player.broadcastBreakEvent(EquipmentSlot.MAINHAND));
    player.swing(hand);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> lines,
      TooltipFlag flag) {
    lines.add(Component.translatable(Translations.Tips.CROWBAR_DESC)
        .withStyle(ChatFormatting.ITALIC));
  }

  private static void removeExtraBlocks(Level level, int enchantmentLevel, BlockPos pos,
      BlockState state, Player player) {
    if (enchantmentLevel > 0) {
      LevelUtil.playerRemoveBlock(level, pos, player);
      checkBlocks(level, enchantmentLevel, pos, player);
    }
  }

  private static void checkBlock(Level level, int enchantmentLevel, BlockPos pos, Player player) {
    var state = level.getBlockState(pos);
    if (player.hasCorrectToolForDrops(state)) {
      removeExtraBlocks(level, enchantmentLevel - 1, pos, state, player);
    }
  }

  private static void checkBlocks(Level level, int enchantmentLevel, BlockPos pos, Player player) {
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
