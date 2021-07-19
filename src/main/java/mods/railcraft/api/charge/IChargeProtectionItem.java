/*------------------------------------------------------------------------------
 Copyright (c) CovertJaguar, 2011-2020

 This work (the API) is licensed under the "MIT" License,
 see LICENSE.md for details.
 -----------------------------------------------------------------------------*/

package mods.railcraft.api.charge;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

/**
 * Implemented by items like the Overalls to prevent Charge based damage.
 *
 * Created by CovertJaguar on 10/18/2018 for Railcraft.
 *
 * @author CovertJaguar <http://www.railcraft.info>
 */
public interface IChargeProtectionItem {

  /**
   * Called to determine if the item can currently provide protection against Charge damage.
   *
   * @param owner owner of the item
   * @return true if the item is currently providing protection.
   */
  default boolean isZapProtectionActive(ItemStack stack, LivingEntity owner) {
    return true;
  }

  /**
   * Called when charge damage is being done and the target is wearing a Charge protection item. You
   * can block all or just some of the damage to the entity, defaults to all. The damage or other
   * cost done to the item is up to the implementation.
   *
   * @param owner owner of the item
   * @param attackDamage damage to be done to the owner
   * @return A ZapResult object with the resulting stack and damage prevented.
   */
  default ZapResult zap(ItemStack stack, LivingEntity owner, float attackDamage) {
    ItemStack resultStack;
    if (owner.getRandom().nextInt(150) == 0
        && stack.hurt(1, owner.getRandom(),
            owner instanceof ServerPlayerEntity ? (ServerPlayerEntity) owner : null))
      resultStack = ItemStack.EMPTY;
    else
      resultStack = stack;
    return new ZapResult(resultStack, attackDamage);
  }

  class ZapResult {

    public final ItemStack stack;
    public final float damagePrevented;

    public ZapResult(ItemStack stack, float damagePrevented) {
      this.stack = stack;
      this.damagePrevented = damagePrevented;
    }
  }
}
