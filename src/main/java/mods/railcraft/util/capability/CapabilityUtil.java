package mods.railcraft.util.capability;

import java.util.function.Supplier;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.nbt.Tag;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.common.util.NonNullSupplier;

public class CapabilityUtil {

  @SafeVarargs
  public static <C extends INBTSerializable<T>, T extends Tag>
  ICapabilityProvider serializableProvider(
      Supplier<T> emptyTag, NonNullSupplier<C> instanceSupplier,
      Capability<? super C>... capabilities) {
    return new SerializableCapabilityProvider<>(emptyTag, LazyOptional.of(instanceSupplier),
        ReferenceSet.of(capabilities), null);
  }
}
