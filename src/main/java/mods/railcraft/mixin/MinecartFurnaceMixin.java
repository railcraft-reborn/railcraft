package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.entity.vehicle.MinecartFurnace;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@Mixin(value = MinecartFurnace.class)
public abstract class MinecartFurnaceMixin extends AbstractMinecart {

  @Shadow
  private int fuel;

  @Shadow
  public double xPush;
  @Shadow
  public double zPush;

  protected MinecartFurnaceMixin(EntityType<?> type, Level level) {
    super(type, level);
  }

  /**
   * Refactors the fuel validation logic:<br>
   * instead of relying on a fixed ingredient check and adding a constant fuel value,<br>
   * the system now derives the fuel increment directly from the item's burn time.<br>
   * This allows support for variable fuel sources while still enforcing<br>
   * the maximum fuel capacity constraint.<br>
   */
  @Overwrite
  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    var ret = super.interact(player, hand);
    if (ret.consumesAction()) {
      return ret;
    }
    ItemStack itemstack = player.getItemInHand(hand);
    var burnTime = itemstack.getBurnTime(null);
    if (burnTime > 0 && this.fuel + burnTime <= 32000) {
      if (!player.getAbilities().instabuild) {
        var craftRemainder = itemstack.getCraftingRemainingItem();
        itemstack.shrink(1);
        if (itemstack.isEmpty()) {
          player.setItemInHand(hand, craftRemainder);
        }
      }

      this.fuel += burnTime;
    }

    if (this.fuel > 0) {
      this.xPush = this.getX() - player.getX();
      this.zPush = this.getZ() - player.getZ();
    }

    return InteractionResult.sidedSuccess(this.level().isClientSide());
  }
}
