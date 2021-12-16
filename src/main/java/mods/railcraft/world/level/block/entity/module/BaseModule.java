package mods.railcraft.world.level.block.entity.module;

public abstract class BaseModule implements Module {

  protected final ModuleProvider provider;

  public BaseModule(ModuleProvider provider) {
    this.provider = provider;
  }

  @Override
  public ModuleProvider getProvider() {
    return this.provider;
  }
}
