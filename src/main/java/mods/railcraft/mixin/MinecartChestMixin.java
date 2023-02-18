package mods.railcraft.mixin;

import org.spongepowered.asm.mixin.Mixin;
import mods.railcraft.util.ValidateSlots;
import mods.railcraft.util.container.StackFilter;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.AbstractMinecartContainer;
import net.minecraft.world.entity.vehicle.MinecartChest;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

@ValidateSlots
@Mixin(MinecartChest.class)
public abstract class MinecartChestMixin extends AbstractMinecartContainer {

  protected MinecartChestMixin(EntityType<?> type, Level level) {
    super(type, level);
  }

  @Override
  public boolean canPlaceItem(int index, ItemStack itemStack) {
    return StackFilter.CARGO.test(itemStack);
  }
}
