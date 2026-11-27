package mods.railcraft.loot;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.UnaryProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProvider;
import net.minecraft.world.level.storage.loot.providers.number.ints.ContextIntProviders;
import net.neoforged.neoforge.common.loot.NeoForgeLootContextParams;

/**
 * Returns the wrapped value while the queried stack still has charges left (damage below max
 * damage), {@code 0} otherwise. Used to stop discharged firestones from burning as fuel.
 */
public record ChargedValue(Holder<ContextIntProvider> input)
    implements ContextIntProvider, UnaryProvider<ContextIntProvider> {

  public static final MapCodec<ChargedValue> MAP_CODEC =
      UnaryProvider.codec(ContextIntProviders.CODEC, ChargedValue::new);

  @Override
  public MapCodec<ChargedValue> codec() {
    return MAP_CODEC;
  }

  @Override
  public int getIntUnsafe(LootContext context) {
    var stack = context.getOptional(NeoForgeLootContextParams.QUERIED_STACK);
    if (stack != null) {
      int maxDamage = stack.getOrDefault(DataComponents.MAX_DAMAGE, 0);
      if (maxDamage > 0 && stack.getOrDefault(DataComponents.DAMAGE, 0) >= maxDamage) {
        return 0;
      }
    }
    return this.input.value().getIntUnsafe(context);
  }
}
