package mods.railcraft.util.registration;

import java.util.function.Function;
import java.util.function.Supplier;
import mods.railcraft.api.core.RailcraftConstants;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class RailcraftDeferredRegister<T> extends DeferredRegister<T> {

  private final Function<ResourceKey<T>, ? extends DeferredHolder<T, ?>> holderCreator;

  public RailcraftDeferredRegister(ResourceKey<? extends Registry<T>> registryKey,
      Function<ResourceKey<T>, ? extends DeferredHolder<T, ?>> holderCreator) {
    super(registryKey, RailcraftConstants.ID);
    this.holderCreator = holderCreator;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <I extends T> DeferredHolder<T, I> register(String name, Function<ResourceLocation, ? extends I> func) {
    return super.register(name, func);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> sup) {
    return super.register(name, sup);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected <I extends T> DeferredHolder<T, I> createHolder(ResourceKey<? extends Registry<T>> registryKey, ResourceLocation key) {
    return (DeferredHolder<T, I>) holderCreator.apply(ResourceKey.create(registryKey, key));
  }
}