package mods.railcraft.world.item;

import java.util.concurrent.atomic.AtomicReference;
import org.jspecify.annotations.Nullable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
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
  public @Nullable ItemStackTemplate getCraftingRemainder(ItemInstance instance) {
    var damage = instance.getOrDefault(DataComponents.DAMAGE, 0);
    var maxDamage = instance.getOrDefault(DataComponents.MAX_DAMAGE, 0);
    double damageLevel = maxDamage == 0 ? 0.0D : (double) damage / (double) maxDamage;
    if (this.random.nextDouble() < damageLevel * 0.0001) {
      return new ItemStackTemplate(RailcraftItems.RAW_FIRESTONE.get());
    }

    var damagedStack = new ItemStack(instance.typeHolder());
    damagedStack.set(DataComponents.DAMAGE, damage);
    var newStack = new AtomicReference<>(damagedStack);
    if (CommonHooks.getCraftingPlayer() instanceof ServerPlayer serverPlayer) {
      newStack.get().hurtAndBreak(1, serverPlayer.level(), serverPlayer,
          __ -> newStack.set(ItemStack.EMPTY));
    }
    var remainder = newStack.get();
    return remainder.isEmpty() ? null : ItemStackTemplate.fromNonEmptyStack(remainder);
  }
}
