package mods.railcraft.util.capability;

import java.util.function.Supplier;
import it.unimi.dsi.fastutil.objects.ReferenceSet;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullSupplier;

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
