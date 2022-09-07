package mods.railcraft.world.item;

import java.util.function.Supplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

public class FluidBottleItem extends Item {

  public static final int QUANTITY = 250;

  private final Supplier<? extends Fluid> fluidSupplier;

  public FluidBottleItem(Supplier<? extends Fluid> supplier, Properties properties) {
    super(properties);
    this.fluidSupplier = supplier;
  }

  public Fluid getFluid() {
    return fluidSupplier.get();
  }
}
