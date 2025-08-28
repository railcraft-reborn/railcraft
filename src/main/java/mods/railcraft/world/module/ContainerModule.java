package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;

public abstract class ContainerModule<T extends ModuleProvider> extends BaseModule<T>
    implements ForwardingContainer {

  protected final AdvancedContainer container;

  protected ContainerModule(T provider, int size) {
    super(provider);
    this.container = new AdvancedContainer(size).listener(provider);
  }

  @Override
  public boolean stillValid(Player player) {
    return this.provider.isStillValid(player);
  }

  @Override
  public Container container() {
    return this.container;
  }

  @Override
  public CompoundTag serializeNBT(HolderLookup.Provider provider) {
    var tag = super.serializeNBT(provider);
    tag.put(CompoundTagKeys.CONTAINER, this.container.createTag(provider));
    return tag;
  }

  @Override
  public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
    super.deserializeNBT(provider, tag);
    tag.getList(CompoundTagKeys.CONTAINER).ifPresent(listTag -> {
      this.container.fromTag(listTag, provider);
    });
  }
}
