package mods.railcraft.util;

import java.util.Optional;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.datamaps.RailcraftDataMaps;
import mods.railcraft.world.item.RefinedFirestoneItem;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.floats.ResolvableFloat;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public final class FuelUtil {
  private static final ContextKeySet FUEL_CONTEXT = new ContextKeySet.Builder()
      .required(NeoForgeLootContextParams.QUERIED_STACK).build();

  private FuelUtil() {}

  public static CookingFuel cookingFuel(int ticks) {
    return new CookingFuel(new ResolvableInt.Constant(ticks),
        new ResolvableFloat.Constant(1));
  }

  public static int burnTime(ItemStack stack, Level level) {
    var fuel = stack.get(DataComponents.COOKING_FUEL);
    if (fuel == null || stack.isEmpty()) {
      return 0;
    }
    if (stack.getItem() instanceof RefinedFirestoneItem firestone) {
      return firestone.burnTime(stack);
    }
    if (!(level instanceof ServerLevel serverLevel)) {
      return fuel.burnTime() instanceof ResolvableInt.Constant constant ? constant.value() : 1;
    }
    var params = new LootParams.Builder(serverLevel)
        .withParameter(NeoForgeLootContextParams.QUERIED_STACK, stack).create(FUEL_CONTEXT);
    var context = new LootContext.Builder(params).create(Optional.empty());
    return ResolvableInt.getFromItem(stack, DataComponents.COOKING_FUEL, CookingFuel::burnTime, context, 0);
  }
  @SuppressWarnings("deprecation")
  public static float getFuelValue(FluidResource fluidResource) {
    var fluidHeat = fluidResource.getFluid().builtInRegistryHolder().getData(RailcraftDataMaps.FLUID_HEAT);
    if (fluidHeat == null) {
      return 0;
    }
    var fuelMultiplier = RailcraftConfig.SERVER.fuelMultiplier.get().floatValue();
    return fuelMultiplier * fluidHeat.heatValuePerBucket();
  }

  public static float getFuelValueForSize(FluidStack fluid) {
    return getFuelValue(FluidResource.of(fluid)) * fluid.getAmount() / 1000;
  }
}
