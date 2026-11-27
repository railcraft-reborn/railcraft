package mods.railcraft.util;

import java.util.Optional;
import mods.railcraft.RailcraftConfig;
import mods.railcraft.datamaps.RailcraftDataMaps;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.context.ContextKeySet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CookingFuel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.providers.number.ints.ResolvableInt;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.fluid.FluidResource;

public final class FuelUtil {

  private static final ContextKeySet FUEL_PARAMS = new ContextKeySet.Builder()
      .optional(NeoForgeLootContextParams.QUERIED_STACK)
      .build();

  private FuelUtil() {
  }

  /**
   * Whether the stack can be burned as solid fuel. Only checks for the
   * {@link DataComponents#COOKING_FUEL} component, so it is safe to call on the client.
   */
  public static boolean isFuel(ItemStack itemStack) {
    if (itemStack.isEmpty() || !itemStack.has(DataComponents.COOKING_FUEL)) {
      return false;
    }
    // Discharged firestones keep the component but no longer burn
    return !itemStack.isDamageableItem()
        || itemStack.getDamageValue() < itemStack.getMaxDamage();
  }

  /**
   * Burn time, in ticks, of the stack used as solid fuel. Burn times are data-driven context
   * providers that can only be resolved on the server: on the client this always returns 0.
   */
  public static int getBurnTime(Level level, ItemStack itemStack) {
    if (!(level instanceof ServerLevel serverLevel) || !isFuel(itemStack)) {
      return 0;
    }
    var context = new LootContext.Builder(new LootParams.Builder(serverLevel)
        .withParameter(NeoForgeLootContextParams.QUERIED_STACK, itemStack)
        .create(FUEL_PARAMS))
        .create(Optional.empty());
    return ResolvableInt.getFromItem(itemStack, DataComponents.COOKING_FUEL,
        CookingFuel::burnTime, context, 0);
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
