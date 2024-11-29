package mods.railcraft.world.item;

import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.common.CommonHooks;

public class CrackedFirestoneItem extends RefinedFirestoneItem {

  public CrackedFirestoneItem(Properties properties) {
    super(100, true, properties);
  }

  public static ItemStack getItemEmpty() {
    var itemStack = RailcraftItems.CRACKED_FIRESTONE.get().getDefaultInstance();
    itemStack.setDamageValue(CHARGES - 1);
    return itemStack;
  }

  @Override
  public ItemStack getCraftingRemainder(ItemStack itemStack) {
    double damageLevel = (double) itemStack.getDamageValue() / (double) itemStack.getMaxDamage();
    if (random.nextDouble() < damageLevel * 0.0001) {
      return RailcraftItems.RAW_FIRESTONE.get().getDefaultInstance();
    }
    var newStack = new AtomicReference<>(itemStack.copyWithCount(1));
    if (CommonHooks.getCraftingPlayer() instanceof ServerPlayer serverPlayer) {
      newStack.get().hurtAndBreak(1, serverPlayer.serverLevel(), serverPlayer,
          __ -> newStack.set(ItemStack.EMPTY));
    }
    return newStack.get();
  }
}
