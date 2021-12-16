package mods.railcraft.world.level.block.entity.module;

import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerTools;
import mods.railcraft.util.container.ForwardingContainer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public abstract class ContainerModule extends BaseModule implements ForwardingContainer {

  protected final AdvancedContainer container;

  protected ContainerModule(ModuleProvider provider, int size) {
    super(provider);
    this.container = new AdvancedContainer(size);
  }

  protected final void dropItem(ItemStack stack) {
    ContainerTools.dropItem(stack, this.provider.getLevel(), this.provider.getBlockPos());
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
    var tag = new CompoundTag();
    tag.put("container", this.container.serializeNBT());
    return tag;
  }

  @Override
  public void deserializeNBT(CompoundTag tag) {
    this.container.deserializeNBT(tag.getList("container", Tag.TAG_COMPOUND));
  }
}
