package mods.railcraft.world.item;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import com.google.common.collect.Sets;
import mods.railcraft.api.items.Crowbar;
import mods.railcraft.plugins.WorldPlugin;
import mods.railcraft.world.item.enchantment.RailcraftEnchantments;
import mods.railcraft.world.level.block.RailcraftToolTypes;
import mods.railcraft.world.level.block.track.ElevatorTrackBlock;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.item.ToolItem;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

public class CrowbarItem extends ToolItem implements Crowbar {

  private static final int BOOST_DAMAGE = 1;
  private final Set<Class<? extends Block>> shiftRotations = new HashSet<>();
  private final Set<Class<? extends Block>> bannedRotations = new HashSet<>();

  public CrowbarItem(float attackDamageIn, float attackSpeedIn, IItemTier tier,
      Properties properties) {
    super(
        attackDamageIn, attackSpeedIn, tier,
        Sets.newHashSet(Blocks.RAIL, Blocks.DETECTOR_RAIL, Blocks.POWERED_RAIL,
            Blocks.ACTIVATOR_RAIL),
        properties.addToolType(RailcraftToolTypes.CROWBAR, 2));
    shiftRotations.add(LeverBlock.class);
    shiftRotations.add(AbstractButtonBlock.class);
    shiftRotations.add(ChestBlock.class);
    bannedRotations.add(AbstractRailBlock.class);
  }

  @Override
  public boolean doesSneakBypassUse(ItemStack stack, IWorldReader world, BlockPos pos,
      PlayerEntity player) {
    return true;
  }

  private boolean isShiftRotation(Class<? extends Block> cls) {
    return shiftRotations.stream().anyMatch(shift -> shift.isAssignableFrom(cls));
  }

  private boolean isBannedRotation(Class<? extends Block> cls) {
    return bannedRotations.stream().anyMatch(banned -> banned.isAssignableFrom(cls));
  }

  @Override
  public ActionResultType onItemUseFirst(ItemStack itemStack, ItemUseContext context) {
    PlayerEntity player = context.getPlayer();
    Hand hand = context.getHand();
    ItemStack stack = player.getItemInHand(hand);
    World world = context.getLevel();
    BlockPos pos = context.getClickedPos();
    BlockState blockState = world.getBlockState(pos);

    if (WorldPlugin.isAir(world, pos, blockState))
      return ActionResultType.PASS;

    if (player.isShiftKeyDown() != isShiftRotation(blockState.getBlock().getClass()))
      return ActionResultType.PASS;

    if (isBannedRotation(blockState.getBlock().getClass()))
      return ActionResultType.PASS;

    if (!world.isClientSide()) {
      BlockState newBlockState = blockState.rotate(world, pos, Rotation.CLOCKWISE_90);
      if (newBlockState != blockState) {
        world.setBlockAndUpdate(pos, newBlockState);
        player.swing(hand);
        stack.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
        return ActionResultType.SUCCESS;
      }
    }

    return ActionResultType.PASS;
  }

  @Override
  public boolean mineBlock(ItemStack stack, World world, BlockState state, BlockPos pos,
      LivingEntity entityLiving) {
    if (!world.isClientSide())
      if (entityLiving instanceof PlayerEntity) {
        PlayerEntity player = (PlayerEntity) entityLiving;
        if (!player.isShiftKeyDown()) {
          int level = EnchantmentHelper
              .getItemEnchantmentLevel(RailcraftEnchantments.DESTRUCTION.get(), stack) * 2 + 1;
          if (level > 1)
            checkBlock(world, level, pos, player);
        }
      }
    return super.mineBlock(stack, world, state, pos, entityLiving);
  }

  /**
   * Current implementations of this method in child classes do not use the entry argument beside
   * ev. They just raise the damage on the stack.
   */
  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    stack.hurtAndBreak(2, attacker, __ -> attacker.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
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
  public boolean canWhack(PlayerEntity player, Hand hand, ItemStack crowbar, BlockPos pos) {
    return true;
  }

  @Override
  public void onWhack(PlayerEntity player, Hand hand, ItemStack crowbar, BlockPos pos) {
    crowbar.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
    player.swing(hand);
  }

  @Override
  public boolean canLink(PlayerEntity player, Hand hand, ItemStack crowbar,
      AbstractMinecartEntity cart) {
    return player.isShiftKeyDown();
  }

  @Override
  public void onLink(PlayerEntity player, Hand hand, ItemStack crowbar,
      AbstractMinecartEntity cart) {
    crowbar.hurtAndBreak(1, player, __ -> player.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
    player.swing(hand);
  }

  @Override
  public boolean canBoost(PlayerEntity player, Hand hand, ItemStack crowbar,
      AbstractMinecartEntity cart) {
    return !player.isShiftKeyDown();
  }

  @Override
  public void onBoost(PlayerEntity player, Hand hand, ItemStack crowbar,
      AbstractMinecartEntity cart) {
    crowbar.hurtAndBreak(BOOST_DAMAGE, player,
        __ -> player.broadcastBreakEvent(EquipmentSlotType.MAINHAND));
    player.swing(hand);
  }

  @Override
  public void appendHoverText(ItemStack stack, @Nullable World world, List<ITextComponent> info,
      ITooltipFlag adv) {
    info.add(new TranslationTextComponent("item.railcraft.tool.crowbar.tips"));
  }

  private void removeExtraBlocks(World world, int level, BlockPos pos, BlockState state,
      PlayerEntity player) {
    if (level > 0) {
      WorldPlugin.playerRemoveBlock(world, pos, player);
      checkBlocks(world, level, pos, player);
    }
  }

  private void checkBlock(World world, int level, BlockPos pos, PlayerEntity player) {
    BlockState state = world.getBlockState(pos);
    if (AbstractRailBlock.isRail(state) || state.getBlock() instanceof ElevatorTrackBlock
        || state.isToolEffective(RailcraftToolTypes.CROWBAR))
      removeExtraBlocks(world, level - 1, pos, state, player);
  }

  private void checkBlocks(World world, int level, BlockPos pos, PlayerEntity player) {
    // NORTH
    checkBlock(world, level, pos.offset(0, 0, -1), player);
    checkBlock(world, level, pos.offset(0, 1, -1), player);
    checkBlock(world, level, pos.offset(0, -1, -1), player);
    // SOUTH
    checkBlock(world, level, pos.offset(0, 0, 1), player);
    checkBlock(world, level, pos.offset(0, 1, 1), player);
    checkBlock(world, level, pos.offset(0, -1, 1), player);
    // EAST
    checkBlock(world, level, pos.offset(1, 0, 0), player);
    checkBlock(world, level, pos.offset(1, 1, 0), player);
    checkBlock(world, level, pos.offset(1, -1, 0), player);
    // WEST
    checkBlock(world, level, pos.offset(-1, 0, 0), player);
    checkBlock(world, level, pos.offset(-1, 1, 0), player);
    checkBlock(world, level, pos.offset(-1, -1, 0), player);
    // UP_DOWN
    checkBlock(world, level, pos.above(), player);
    checkBlock(world, level, pos.below(), player);
  }
}
