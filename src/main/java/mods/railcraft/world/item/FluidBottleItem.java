package mods.railcraft.world.item;

import java.util.function.Supplier;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.capability.FluidBottleWrapper;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class FluidBottleItem extends Item {

  public static final int QUANTITY = 333;

  private final Supplier<? extends Fluid> fluidSupplier;

  public FluidBottleItem(Supplier<? extends Fluid> supplier, Properties properties) {
    super(properties);
    this.fluidSupplier = supplier;
  }

  public Fluid getFluid() {
    return fluidSupplier.get();
  }

  @Nullable
  @Override
  public ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
    return new FluidBottleWrapper(stack);
  }
}
