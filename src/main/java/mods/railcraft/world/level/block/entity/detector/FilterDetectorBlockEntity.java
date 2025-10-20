package mods.railcraft.world.level.block.entity.detector;

import java.util.stream.Stream;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public abstract class FilterDetectorBlockEntity extends DetectorBlockEntity
    implements MenuProvider, ContainerManipulator<ModifiableSlotAccessor> {

  protected final AdvancedContainer invFilters;

  protected FilterDetectorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState, int inventorySize) {
    super(type, blockPos, blockState);
    this.invFilters = new AdvancedContainer(inventorySize).listener(this).phantom();
  }

  public AdvancedContainer getInvFilters() {
    return invFilters;
  }

  @Override
  public Stream<ModifiableSlotAccessor> stream() {
    return this.invFilters.stream();
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.CONTAINER, this.invFilters);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.CONTAINER, this.invFilters);
  }
}
