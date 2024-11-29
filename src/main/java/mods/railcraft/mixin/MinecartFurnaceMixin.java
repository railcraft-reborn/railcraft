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
import net.minecraft.world.phys.Vec3;

@Mixin(value = MinecartFurnace.class)
public abstract class MinecartFurnaceMixin extends AbstractMinecart {

  @Shadow
  private int fuel;

  @Shadow
  public Vec3 push;

  protected MinecartFurnaceMixin(EntityType<?> type, Level level) {
    super(type, level);
  }

  /**
   * Replace ItemTags.FURNACE_MINECART_FUEL with itemstack.getBurnTime(...)
   */
  @Overwrite
  @Override
  public InteractionResult interact(Player player, InteractionHand hand) {
    var ret = super.interact(player, hand);
    if (ret.consumesAction()) {
      return ret;
    }
    ItemStack itemstack = player.getItemInHand(hand);
    var burnTime = itemstack.getBurnTime(null, this.level().fuelValues());
    if (burnTime > 0 && this.fuel + burnTime <= 32000) {
      if (!player.getAbilities().instabuild) {
        var craftRemainder = itemstack.getCraftingRemainder();
        itemstack.shrink(1);
        if (itemstack.isEmpty()) {
          player.setItemInHand(hand, craftRemainder);
        }
      }

      this.fuel += burnTime;
    }

    if (this.fuel > 0) {
      this.push = this.position().subtract(player.position()).horizontal();
    }

    return InteractionResult.SUCCESS;
  }
}
