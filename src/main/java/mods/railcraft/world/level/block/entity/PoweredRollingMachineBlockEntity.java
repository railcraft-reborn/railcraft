package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.charge.Charge;
import mods.railcraft.util.container.CombinedInvWrapper;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import mods.railcraft.world.level.block.PoweredRollingMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class PoweredRollingMachineBlockEntity extends ManualRollingMachineBlockEntity {

  private static final int CHARGE_PER_TICK = 10;
  private final IItemHandler itemHandler;

  public PoweredRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(), blockPos, blockState);
    this.itemHandler = new CombinedInvWrapper(this.craftMatrix, this.getInvResult());
  }

  @Override
  protected void progress() {
    if (this.access().useCharge(CHARGE_PER_TICK, false)) {
      super.progress();
    }
  }

  @Override
  public void preRemoveSideEffects(BlockPos pos, BlockState state) {
    Containers.dropContents(level, pos, this.getInvResult());
    Containers.dropContents(level, pos, this.craftMatrix);
    Containers.updateNeighboursAfterDestroy(state, this.level, pos);

    if (level instanceof ServerLevel serverLevel) {
      ((PoweredRollingMachineBlock) state.getBlock()).deregisterNode(serverLevel, pos);
    }
  }

  private Charge.Access access() {
    return Charge.distribution
        .network((ServerLevel) this.level)
        .access(this.blockPos());
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
    return new PoweredRollingMachineMenu(containerId, inventory, this);
  }

  public IItemHandler getItemCap(@Nullable Direction side) {
    return this.itemHandler;
  }
}
