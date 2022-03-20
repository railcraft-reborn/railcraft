package mods.railcraft.util.container.manipulator;

import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.item.ItemStack;

/**
 * An implementation of {@link ContainerManipulator} for Minecraft's {@link WorldlyContainer}.
 * 
 * @author Sm0keySa1m0n
 */
@FunctionalInterface
public interface WorldlyContainerManipulator extends VanillaContainerManipulator {

  @Override
  WorldlyContainer getContainer();

  @Override
  default Stream<ModifiableSlotAccessor> stream() {
    return Arrays.stream(this.getContainer().getSlotsForFace(Direction.DOWN))
        .mapToObj(index -> new WorldlySlotAccessor(this.getContainer(), index));
  }

  public class WorldlySlotAccessor extends VanillaSlotAccessor<WorldlyContainer> {

    public WorldlySlotAccessor(WorldlyContainer container, int index) {
      super(container, index);
    }

    @Override
    public boolean isValid(ItemStack itemStack) {
      return this.getContainer().canPlaceItemThroughFace(this.getIndex(), itemStack,
          Direction.DOWN);
    }

    @Override
    public boolean canRemoveItem() {
      return this.getContainer().canTakeItemThroughFace(this.getIndex(), this.getItem(),
          Direction.DOWN);
    }
  }
}
