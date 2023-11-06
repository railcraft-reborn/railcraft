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
import net.neoforged.neoforge.common.CommonHooks;

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
   * Replace fuel checks with
   * {@link CommonHooks#getBurnTime(ItemStack, net.minecraft.world.item.crafting.RecipeType)}.
   */
  @Overwrite
  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    InteractionResult ret = super.interact(player, hand);
    if (ret.consumesAction()) {
      return ret;
    }
    ItemStack itemstack = player.getItemInHand(hand);
    var burnTime = CommonHooks.getBurnTime(itemstack, null);
    if (burnTime > 0 && this.fuel + burnTime <= 32000) {
      if (!player.getAbilities().instabuild) {
        itemstack.shrink(1);
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
