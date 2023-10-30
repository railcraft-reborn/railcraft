package mods.railcraft.util.capability;

import java.util.Set;
import org.jetbrains.annotations.Nullable;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.common.util.NonNullFunction;

class SimpleCapabilityProvider<C> implements ICapabilityProvider {

  protected final LazyOptional<C> instance;
  protected final Set<Capability<? super C>> capabilities;

  @Nullable
  protected final NonNullFunction<C, ICapabilityProvider> instanceMapper;

  public SimpleCapabilityProvider(LazyOptional<C> instance,
      Set<Capability<? super C>> capabilities,
      @Nullable NonNullFunction<C, ICapabilityProvider> instanceMapper) {
    this.instance = instance;
    this.capabilities = capabilities;
    this.instanceMapper = instanceMapper;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
    if (this.capabilities.contains(cap)) {
      return this.instance.cast();
    } else if (this.instanceMapper != null) {
      return this.instance
          .lazyMap(this.instanceMapper)
          .lazyMap(provider -> provider.getCapability(cap, side))
          .orElse(LazyOptional.empty());
    }
    return LazyOptional.empty();
  }
}
