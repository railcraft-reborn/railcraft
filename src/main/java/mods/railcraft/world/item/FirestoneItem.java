package mods.railcraft.world.item;

import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import mods.railcraft.Translations.Tips;
import mods.railcraft.util.LevelUtil;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.world.entity.FirestoneItemEntity;
import mods.railcraft.world.level.block.RailcraftBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.NotNull;

/**
 * @author CovertJaguar <https://www.railcraft.info/>
 */
public class FirestoneItem extends Item {

  public static final Predicate<ItemStack> SPAWNS_FIRE = itemStack -> {
    if (itemStack.isEmpty())
      return false;
    if (itemStack.is(RailcraftItems.RAW_FIRESTONE.get()))
      return true;
    if (itemStack.is(RailcraftItems.CUT_FIRESTONE.get()))
      return true;
    if (itemStack.is(RailcraftItems.CRACKED_FIRESTONE.get()))
      return true;
    return ContainerTools.isStackEqualToBlock(itemStack, RailcraftBlocks.FIRESTONE_ORE.get());
  };

  public FirestoneItem(Properties properties) {
    super(properties);
  }

  @Override
  public boolean hasCustomEntity(ItemStack itemStack) {
    return true;
  }

  @Override
  @NotNull
  public FirestoneItemEntity createEntity(Level level, Entity entity, ItemStack itemStack) {
    return createEntityItem(level, entity, itemStack);
  }

  @Override
  public boolean isRepairable(ItemStack itemStack) {
    return false;
  }

  @Override
  public boolean isEnchantable(ItemStack itemStack) {
    return false;
  }

  @NotNull
  public static FirestoneItemEntity createEntityItem(Level level, Entity entity, ItemStack itemStack) {
    var firestone = new FirestoneItemEntity(level, entity.position(), itemStack);
    firestone.setThrower(((ItemEntity) entity).getThrower());
    firestone.setDeltaMovement(entity.getDeltaMovement());
    firestone.setDefaultPickUpDelay();
    return firestone;
  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId,
      boolean isSelected) {
    if (entity instanceof Player player) {
      if (level.getRandom().nextInt(12) % 4 == 0) {
        var pos = new BlockPos(player.position());
        trySpawnFire(player.level, pos, stack, player);
      }
    }
  }

  public static boolean trySpawnFire(Level level, BlockPos pos, ItemStack stack, Player player) {
    if (!SPAWNS_FIRE.test(stack))
      return false;
    boolean spawnedFire = false;
    for (int i = 0; i < stack.getCount(); i++) {
      spawnedFire |= spawnFire(level, pos, player);
    }
    if (spawnedFire && stack.isDamageableItem()
        && stack.getDamageValue() < stack.getMaxDamage() - 1)
      stack.hurtAndBreak(1, player, t -> {});
    return spawnedFire;
  }

  public static boolean spawnFire(Level level, BlockPos pos, @Nullable Player player) {
    var random = level.getRandom();
    int x = pos.getX() - 5 + random.nextInt(12);
    int y = pos.getY() - 5 + random.nextInt(12);
    int z = pos.getZ() - 5 + random.nextInt(12);

    y = Mth.clamp(y, level.getMinBuildHeight() + 2, level.getMaxBuildHeight() - 1);

    BlockPos firePos = new BlockPos(x, y, z);
    return canBurn(level, firePos)
        && LevelUtil.setBlockState(level, firePos, Blocks.FIRE.defaultBlockState(), player);
  }

  private static boolean canBurn(Level level, BlockPos pos) {
    if (level.getBlockState(pos).isAir()) {
      return false;
    }
    for (var side : Direction.values()) {
      var offset = pos.relative(side);
      var offsetBlockState = level.getBlockState(offset);
      if (!offsetBlockState.isAir() && offsetBlockState.getMaterial() != Material.FIRE) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
      TooltipFlag isAdvanced) {
    if(stack.is(RailcraftItems.RAW_FIRESTONE.get())) {
      tooltipComponents.add(Component.translatable(Tips.RAW_FIRESTONE).withStyle(ChatFormatting.GRAY));
    } else if (stack.is(RailcraftItems.CUT_FIRESTONE.get())) {
      tooltipComponents.add(Component.translatable(Tips.CUT_FIRESTONE).withStyle(ChatFormatting.GRAY));
    }
  }
}
