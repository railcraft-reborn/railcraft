package mods.railcraft.world.level.block.entity;

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
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.VanillaContainerWrapper;

public abstract class ContainerBlockEntity extends RailcraftBlockEntity
    implements ForwardingContainer, ContainerManipulator<ModifiableSlotAccessor> {

  private AdvancedContainer container;
  private final ResourceHandler<ItemResource> itemHandler;
  private Map<Direction, ResourceHandler<ItemResource>> directionalItemHandlers = new EnumMap<>(Direction.class);

  public ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
    this(type, blockPos, blockState, 0);
  }

  protected ContainerBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState,
      int size) {
    super(type, blockPos, blockState);
    this.container = new AdvancedContainer(size).listener((Container) this);
    this.itemHandler = VanillaContainerWrapper.of(this);
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

  public ResourceHandler<ItemResource> getItemCap(@Nullable Direction side) {
    if (side == null) {
      return this.itemHandler;
    }
    return this.directionalItemHandlers
        .computeIfAbsent(side, __ -> ItemHandlerFactory.wrap(this, side));
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.CONTAINER, this.container);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.CONTAINER, this.container);
  }
}
