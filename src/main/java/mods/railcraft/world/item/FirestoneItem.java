package mods.railcraft.world.item;

import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.Translations;
import mods.railcraft.world.entity.FirestoneItemEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;

public class FirestoneItem extends Item {

  private final boolean spawnsFire;

  public FirestoneItem(boolean spawnsFire, Properties properties) {
    super(properties);
    this.spawnsFire = spawnsFire;
  }

  public boolean spawnsFire() {
    return this.spawnsFire;
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

  public void fillItemCategory(CreativeModeTab.Output output) {
    output.accept(new ItemStack(this));
    var item = new ItemStack(this);
    if (item.isDamageableItem()) {
      item.setDamageValue(item.getMaxDamage() - 1);
      output.accept(item);
    }
  }

  @NotNull
  public static FirestoneItemEntity createEntityItem(Level level, Entity entity,
      ItemStack itemStack) {
    var firestone = new FirestoneItemEntity(level, entity.position(), itemStack);
    firestone.setThrower(entity.getUUID());
    firestone.setDeltaMovement(entity.getDeltaMovement());
    firestone.setDefaultPickUpDelay();
    return firestone;
  }

  @Override
  public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId,
      boolean isSelected) {
    if (this.spawnsFire
        && level.getGameRules().getBoolean(GameRules.RULE_DOFIRETICK)
        && entity instanceof Player player
        && level.getRandom().nextInt(12) % 4 == 0) {
      trySpawnFire(player.level(), player.blockPosition(), stack, player);
    }
  }

  public static boolean trySpawnFire(Level level, BlockPos pos, ItemStack stack, Entity entity) {
    boolean spawnedFire = false;
    for (int i = 0; i < stack.getCount(); i++) {
      spawnedFire |= spawnFire(level, pos, entity);
    }
    if (spawnedFire && stack.isDamageableItem()
        && stack.getDamageValue() < stack.getMaxDamage() - 1) {
      if (entity instanceof Player player) {
        stack.hurtAndBreak(1, player, t -> {});
      }
    }
    return spawnedFire;
  }

  public static boolean spawnFire(Level level, BlockPos pos, @Nullable Entity entity) {
    var random = level.getRandom();
    int x = pos.getX() - 5 + random.nextInt(12);
    int y = pos.getY() - 5 + random.nextInt(12);
    int z = pos.getZ() - 5 + random.nextInt(12);

    y = Mth.clamp(y, level.getMinBuildHeight() + 2, level.getMaxBuildHeight() - 1);

    var firePos = new BlockPos(x, y, z);
    var blockState = BaseFireBlock.getState(level, firePos);
    return level.getBlockState(firePos).isAir()
        && blockState.canSurvive(level, firePos)
        && level.setBlockAndUpdate(firePos, blockState);
  }

  @Override
  public void appendHoverText(ItemStack stack, Level level, List<Component> tooltipComponents,
      TooltipFlag isAdvanced) {
    if (stack.is(RailcraftItems.RAW_FIRESTONE.get())) {
      tooltipComponents
          .add(Component.translatable(Translations.Tips.RAW_FIRESTONE)
              .withStyle(ChatFormatting.GRAY));
    } else if (stack.is(RailcraftItems.CUT_FIRESTONE.get())) {
      tooltipComponents
          .add(Component.translatable(Translations.Tips.CUT_FIRESTONE)
              .withStyle(ChatFormatting.GRAY));
    }
  }
}
