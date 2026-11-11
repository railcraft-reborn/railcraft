package mods.railcraft.util;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;
import net.neoforged.neoforge.registries.DeferredRegister;

public record FirestoneFuel(int heat) implements ContextIntProvider {
  public static final MapCodec<FirestoneFuel> CODEC = Codec.INT.fieldOf("heat")
      .xmap(FirestoneFuel::new, FirestoneFuel::heat);
  private static final DeferredRegister<MapCodec<? extends ContextIntProvider>> TYPES =
      DeferredRegister.create(Registries.CONTEXT_INT_PROVIDER_TYPE, RailcraftConstants.ID);

  public static void register(IEventBus bus) {
    TYPES.register("firestone_fuel", () -> CODEC);
    TYPES.register(bus);
  }

  public static ResourceKey<ContextIntProvider> key(int heat) {
    return ResourceKey.create(Registries.CONTEXT_INT_PROVIDER, RailcraftConstants.id("firestone_fuel/" + heat));
  }

  public static void bootstrap(BootstrapContext<ContextIntProvider> context) {
    context.register(key(250), new FirestoneFuel(250));
    context.register(key(100), new FirestoneFuel(100));
  }

  @Override
  public int getIntUnsafe(LootContext context) {
    var stack = context.getOptional(NeoForgeLootContextParams.QUERIED_STACK);
    return stack != null && stack.getOrDefault(DataComponents.DAMAGE, 0)
        < stack.getOrDefault(DataComponents.MAX_DAMAGE, 0) ? this.heat : 0;
  }

  @Override
  public MapCodec<FirestoneFuel> codec() {
    return CODEC;
  }

  @Override
  public void validate(ValidationContext context) {
    // The queried stack is optional in vanilla container processing contexts.
  }
}
