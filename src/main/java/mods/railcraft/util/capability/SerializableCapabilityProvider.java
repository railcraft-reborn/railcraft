package mods.railcraft.util.capability;

import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.nbt.Tag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullFunction;

class SerializableCapabilityProvider<C extends INBTSerializable<T>, T extends Tag>
    extends SimpleCapabilityProvider<C> implements INBTSerializable<T> {

  private final Supplier<T> emptyTag;

  public SerializableCapabilityProvider(
      Supplier<T> emptyTag, LazyOptional<C> instance,
      Set<Capability<? super C>> capabilities,
      NonNullFunction<C, ICapabilityProvider> instanceMapper) {
    super(instance, capabilities, instanceMapper);
    this.emptyTag = emptyTag;
  }

  @Override
  public T serializeNBT() {
    return this.instance.map(C::serializeNBT).orElseGet(this.emptyTag);
  }

  @Override
  public void deserializeNBT(T tag) {
    this.instance.ifPresent(i -> i.deserializeNBT(tag));
  }
}
