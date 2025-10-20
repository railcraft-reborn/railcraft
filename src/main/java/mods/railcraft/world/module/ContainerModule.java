package mods.railcraft.world.module;

import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

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
  public void serialize(ValueOutput valueOutput) {
    super.serialize(valueOutput);
    valueOutput.putChild(CompoundTagKeys.CONTAINER, this.container);
  }

  @Override
  public void deserialize(ValueInput valueInput) {
    super.deserialize(valueInput);
    valueInput.readChild(CompoundTagKeys.CONTAINER, this.container);
  }
}
