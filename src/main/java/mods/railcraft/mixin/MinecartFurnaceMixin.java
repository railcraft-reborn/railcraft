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
import net.minecraftforge.common.ForgeHooks;

@Mixin(MinecartFurnace.class)
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
   * {@link ForgeHooks#getBurnTime(ItemStack, net.minecraft.world.item.crafting.RecipeType)}.
   */
  @Overwrite
  @Override
  public InteractionResult interact(Player pPlayer, InteractionHand pHand) {
    InteractionResult ret = super.interact(pPlayer, pHand);
    if (ret.consumesAction()) {
      return ret;
    }
    ItemStack itemstack = pPlayer.getItemInHand(pHand);
    var burnTime = ForgeHooks.getBurnTime(itemstack, null);
    if (burnTime > 0 && this.fuel + burnTime <= 32000) {
      if (!pPlayer.getAbilities().instabuild) {
        itemstack.shrink(1);
      }

      this.fuel += burnTime;
    }

    if (this.fuel > 0) {
      this.xPush = this.getX() - pPlayer.getX();
      this.zPush = this.getZ() - pPlayer.getZ();
    }

    return InteractionResult.sidedSuccess(this.level().isClientSide);
  }
}
