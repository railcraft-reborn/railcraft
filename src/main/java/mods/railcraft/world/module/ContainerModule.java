package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
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
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put(CompoundTagKeys.CONTAINER, this.container.createTag());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.container.fromTag(tag.getList(CompoundTagKeys.CONTAINER, Tag.TAG_COMPOUND));
  }
}
