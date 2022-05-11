package mods.railcraft.world.module;

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
    return this.provider.stillValid(player);
  }

  @Override
  public Container getContainer() {
    return this.container;
  }

  @Override
  public CompoundTag serializeNBT() {
    var tag = super.serializeNBT();
    tag.put("container", this.container.createTag());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    super.deserializeNBT(tag);
    this.container.fromTag(tag.getList("container", Tag.TAG_COMPOUND));
  }
}
