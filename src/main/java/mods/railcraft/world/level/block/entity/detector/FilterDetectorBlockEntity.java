package mods.railcraft.world.level.block.entity.detector;

import java.util.stream.Stream;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

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
  public void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.loadAdditional(tag, provider);
    this.invFilters.fromTag(tag.getList(CompoundTagKeys.CONTAINER, Tag.TAG_COMPOUND), provider);
  }

  @Override
  public void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
    super.saveAdditional(tag, provider);
    tag.put(CompoundTagKeys.CONTAINER, this.invFilters.createTag(provider));
  }
}
