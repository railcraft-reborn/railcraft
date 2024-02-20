package mods.railcraft.world.level.block.entity.detector;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.util.container.ItemHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class FilterDetectorBlockEntity extends DetectorBlockEntity
    implements MenuProvider, ForwardingContainer, ContainerManipulator<ModifiableSlotAccessor> {

  private final AdvancedContainer invFilters;
  private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
  private Map<Direction, LazyOptional<IItemHandler>> directionalItemHandlers =
      new EnumMap<>(Direction.class);

  protected FilterDetectorBlockEntity(BlockEntityType<?> type, BlockPos blockPos,
      BlockState blockState, int inventorySize) {
    super(type, blockPos, blockState);
    this.invFilters = new AdvancedContainer(inventorySize).listener((Container) this).phantom();
  }

  @Override
  public Stream<ModifiableSlotAccessor> stream() {
    return this.invFilters.stream();
  }

  @Override
  public boolean stillValid(Player player) {
    return this.isStillValid(player);
  }

  @Override
  public Container container() {
    return this.invFilters;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability,
      @Nullable Direction direction) {
    if (capability == ForgeCapabilities.ITEM_HANDLER) {
      return direction == null
          ? this.itemHandler.cast()
          : this.directionalItemHandlers.computeIfAbsent(direction,
              __ -> LazyOptional.of(() -> ItemHandlerFactory.wrap(this, direction))).cast();
    }
    return super.getCapability(capability, direction);
  }

  @Override
  public void load(CompoundTag tag) {
    super.load(tag);
    this.invFilters.fromTag(tag.getList(CompoundTagKeys.CONTAINER, Tag.TAG_COMPOUND));
  }

  @Override
  public void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put(CompoundTagKeys.CONTAINER, this.invFilters.createTag());
  }
}
