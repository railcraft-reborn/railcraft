package mods.railcraft.world.level.block.entity;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ForwardingContainer;
import mods.railcraft.util.container.ItemHandlerFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.wrapper.InvWrapper;

public abstract class ContainerBlockEntity extends RailcraftBlockEntity
    implements ForwardingContainer, ContainerManipulator<ModifiableSlotAccessor> {

  private AdvancedContainer container;
  private IItemHandler itemHandler = new InvWrapper(this);
  private Map<Direction, IItemHandlerModifiable> directionalItemHandlers = new EnumMap<>(Direction.class);

  public ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    this(type, blockPos, blockState, 0);
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
    return this.isStillValid(player);
  }

  @Override
  public Container container() {
    return this.container;
  }

  public IItemHandler getItemCap(@Nullable Direction side) {
    if (side == null) {
      return this.itemHandler;
    }
    return this.directionalItemHandlers
        .computeIfAbsent(side, __ -> ItemHandlerFactory.wrap(this, side));
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
