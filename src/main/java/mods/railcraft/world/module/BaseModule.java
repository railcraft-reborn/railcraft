package mods.railcraft.world.module;

@SharedModule
public abstract class BaseModule<T extends ModuleProvider> implements Module {

  protected final T provider;

  public BaseModule(T provider) {
    this.provider = provider;
  }

  @Override
  public T getProvider() {
    return this.provider;
  }
}
