package mods.railcraft.world.level.block.entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.util.container.ItemHandlerFactory;
import mods.railcraft.util.container.manipulator.ContainerManipulator;
import mods.railcraft.util.container.manipulator.ModifiableSlotAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class ContainerBlockEntity extends RailcraftBlockEntity
    implements ForwardingContainer, ContainerManipulator<ModifiableSlotAccessor> {

  private AdvancedContainer container;
  private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
  private Map<Direction, LazyOptional<IItemHandler>> directionalItemHandlers =
      new EnumMap<>(Direction.class);

  public ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
    this.container = new AdvancedContainer(0).listener((Container) this);
  }

  protected ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      int size) {
    super(type, blockPos, blockState);
    this.container = new AdvancedContainer(size).listener((Container) this);
  }

  protected void setContainerSize(int size) {
    this.container = new AdvancedContainer(size).listener((Container) this);
  }

  @Override
  public Stream<ModifiableSlotAccessor> stream() {
    return this.container.stream();
  }

  @Override
  public boolean stillValid(Player player) {
    return RailcraftBlockEntity.stillValid(this, player);
  }

  @Override
  public Container container() {
    return this.container;
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
    this.container.fromTag(tag.getList("container", Tag.TAG_COMPOUND));
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    tag.put("container", this.container.createTag());
  }
}
