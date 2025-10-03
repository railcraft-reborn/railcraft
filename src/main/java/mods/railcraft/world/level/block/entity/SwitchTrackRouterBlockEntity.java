package mods.railcraft.world.level.block.entity;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import com.mojang.datafixers.util.Either;
import mods.railcraft.api.carts.RollingStock;
import mods.railcraft.api.core.CompoundTagKeys;
import mods.railcraft.api.track.SwitchActuator;
import mods.railcraft.util.container.AdvancedContainer;
import mods.railcraft.util.routing.RouterBlockEntity;
import mods.railcraft.util.routing.RoutingLogic;
import mods.railcraft.util.routing.RoutingLogicException;
import mods.railcraft.world.inventory.SwitchTrackRouterMenu;
import mods.railcraft.world.item.component.RailcraftDataComponents;
import mods.railcraft.world.level.block.track.actuator.SwitchTrackActuatorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.server.players.NameAndId;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class SwitchTrackRouterBlockEntity extends LockableSwitchTrackActuatorBlockEntity
    implements RouterBlockEntity, SwitchActuator {

  private final AdvancedContainer container;
  @Nullable
  private Either<RoutingLogic, RoutingLogicException> logic;
  private Railway railway = Railway.PUBLIC;
  private boolean powered;

  public SwitchTrackRouterBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SWITCH_TRACK_ROUTER.get(), blockPos, blockState);
    this.container = new AdvancedContainer(1).listener((Container) this);
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    super.preRemoveSideEffects(pos, state);
    Containers.updateNeighboursAfterDestroy(state, this.level, pos);
  }

  @Override
  public void neighborChanged() {
    this.powered = this.level.hasNeighborSignal(this.getBlockPos());
    this.setChanged();
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
  protected void saveAdditional(ValueOutput output) {
    super.saveAdditional(output);
    output.putChild(CompoundTagKeys.CONTAINER, this.container);
    output.store(CompoundTagKeys.RAILWAY, Railway.CODEC, this.railway);
    output.putBoolean(CompoundTagKeys.POWERED, this.powered);
  }

  @Override
  protected void loadAdditional(ValueInput input) {
    super.loadAdditional(input);
    this.container.deserialize(input.childOrEmpty(CompoundTagKeys.CONTAINER));
    this.railway = input.read(CompoundTagKeys.RAILWAY, Railway.CODEC).orElse(Railway.PUBLIC);
    this.powered = input.getBooleanOr(CompoundTagKeys.POWERED, false);
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
  public boolean isPowered() {
    return this.powered;
  }

  @Override
  public Optional<Either<RoutingLogic, RoutingLogicException>> logicResult() {
    this.refreshLogic();
    return Optional.ofNullable(this.logic);
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

  @Override
  public boolean shouldSwitch(RollingStock rollingStock) {
    boolean shouldSwitch = this.logic()
        .map(logic -> logic.matches(this, rollingStock))
        .orElse(false);
    if (this.railway == Railway.PRIVATE) {
      shouldSwitch = rollingStock.owner()
          .filter(owner -> owner.equals(this.getOwnerOrThrow()))
          .isPresent();
    }
    SwitchTrackActuatorBlock.setSwitched(
        this.getBlockState(), this.level, this.getBlockPos(), shouldSwitch);
    return shouldSwitch;
  }

  @Override
  public Container container() {
    return this.container;
  }

  @Override
  public boolean stillValid(Player player) {
    return isStillValid(player);
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SwitchTrackRouterMenu(id, inventory, this);
  }
}
