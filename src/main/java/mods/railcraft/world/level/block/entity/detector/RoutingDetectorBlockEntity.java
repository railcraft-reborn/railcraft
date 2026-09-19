package mods.railcraft.world.level.block.entity.detector;


import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.jspecify.annotations.Nullable;
import com.mojang.datafixers.util.Either;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.container.manipulator.ContainerManipulator;
import mods.railcraft.api.container.manipulator.ModifiableSlotAccessor;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.inventory.detector.RoutingDetectorMenu;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.minecart.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class RoutingDetectorBlockEntity extends SecureDetectorBlockEntity implements
    ContainerManipulator<ModifiableSlotAccessor>, RouterBlockEntity {

  private final AdvancedContainer container;
  @Nullable
  private Either<RoutingLogic, RoutingLogicException> logic;
  private Railway railway = Railway.PUBLIC;
  private boolean powered;

  public RoutingDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ROUTING_DETECTOR.get(), blockPos, blockState);
    this.container = new AdvancedContainer(1).listener((Container) this);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    if (this.logic().isEmpty()) {
      return Redstone.SIGNAL_NONE;
    }
    for (var cart : minecarts) {
      var rollingStock = RollingStock.getOrThrow(cart);
      if (this.railway == Railway.PRIVATE) {
        if (rollingStock.owner()
            .filter(owner -> owner.equals(this.getOwnerOrThrow()))
            .isEmpty()) {
          continue;
        }
      }
      if (this.logic()
          .map(logic -> logic.matches(this, rollingStock))
          .orElse(false)) {
        return Redstone.SIGNAL_MAX;
      }
    }
    return Redstone.SIGNAL_NONE;
  }

  @Override
  public void neighborChanged() {
    this.powered = this.level.hasNeighborSignal(this.getBlockPos());
    this.setChanged();
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    Containers.updateNeighboursAfterDestroy(state, this.level, pos);
  }

  @Override
  public Railway getRailway() {
    return this.railway;
  }

  @Override
  public void setRailway(@Nullable NameAndId gameProfile) {
    this.railway = gameProfile == null ? Railway.PUBLIC : Railway.PRIVATE;
    if (!this.isLocked()) {
      this.setOwner(gameProfile);
    }
  }

  @Override
  public Stream<ModifiableSlotAccessor> stream() {
    return this.container.stream();
  }

  @Override
  public Container container() {
    return this.container;
  }

  @Override
  public boolean stillValid(Player player) {
    return this.isStillValid(player);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    input.readChild(CompoundTagKeys.CONTAINER, this.container);
    this.railway = input.read(CompoundTagKeys.RAILWAY, Railway.CODEC).orElse(Railway.PUBLIC);
    this.powered = input.getBooleanOr(CompoundTagKeys.POWERED, false);
  }

  @Override
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.CONTAINER, this.container);
    output.store(CompoundTagKeys.RAILWAY, Railway.CODEC, this.railway);
    output.putBoolean(CompoundTagKeys.POWERED, this.powered);
  }

  @Override
  public void writeToBuf(RegistryFriendlyByteBuf data) {
    super.writeToBuf(data);
    data.writeEnum(this.railway);
    data.writeBoolean(this.powered);
  }

  @Override
  public void readFromBuf(RegistryFriendlyByteBuf data) {
    super.readFromBuf(data);
    this.railway = data.readEnum(Railway.class);
    this.powered = data.readBoolean();
  }

  @Override
  public Optional<Either<RoutingLogic, RoutingLogicException>> logicResult() {
    this.refreshLogic();
    return Optional.ofNullable(this.logic);
  }

  @Override
  public boolean isPowered() {
    return this.powered;
  }

  @Override
  public void resetLogic() {
    this.logic = null;
  }

  private void refreshLogic() {
    if (this.logic == null && !this.container.getItem(0).isEmpty()) {
      var item = this.container.getItem(0);
      if (item.has(RailcraftDataComponents.ROUTING_TABLE_BOOK)) {
        var content = this.loadPages(item);
        try {
          this.logic = Either.left(RoutingLogic.parseTable(content));
        } catch (RoutingLogicException e) {
          this.logic = Either.right(e);
        }
      }
    }
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new RoutingDetectorMenu(id, inventory, this);
  }
}
