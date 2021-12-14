package mods.railcraft.world.level.block.entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.container.ContainerAdaptor;
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
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

public abstract class ContainerBlockEntity extends RailcraftBlockEntity
    implements ForwardingContainer {

  private AdvancedContainer container;
  private LazyOptional<IItemHandler> itemHandler = LazyOptional.of(() -> new InvWrapper(this));
  private Map<Direction, LazyOptional<IItemHandler>> directionalItemHandlers =
      new EnumMap<>(Direction.class);

  public ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    super(type, blockPos, blockState);
    this.container = new AdvancedContainer(0).callbackContainer(this);

  }

  protected ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      int size) {
    super(type, blockPos, blockState);
    this.container = new AdvancedContainer(size).callbackContainer(this);
  }

  protected void setContainerSize(int size) {
    this.container = new AdvancedContainer(size).callbackContainer(this);
  }

  protected Collection<ContainerAdaptor> getAdjacentContainers() {
    List<ContainerAdaptor> containers = new ArrayList<>();
    for (var direction : Direction.values()) {
      var blockEntity = this.level.getBlockEntity(this.getBlockPos().relative(direction));
      if (blockEntity != null) {
        blockEntity
            .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, direction.getOpposite())
            .map(ContainerAdaptor::of)
            .ifPresent(containers::add);
      }
    }
    return containers;
  }

  @Override
  public boolean stillValid(Player player) {
    return RailcraftBlockEntity.stillValid(this, player);
  }

  @Override
  public Container getContainer() {
    return this.container;
  }

  @Override
  public <T> LazyOptional<T> getCapability(Capability<T> capability,
      @Nullable Direction direction) {
    if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
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
    tag.put("inventory", this.container.serializeNBT());
  }

  @Override
  protected void saveAdditional(CompoundTag tag) {
    super.saveAdditional(tag);
    this.container.deserializeNBT(tag.getList("inventory", Tag.TAG_COMPOUND));
  }
}
