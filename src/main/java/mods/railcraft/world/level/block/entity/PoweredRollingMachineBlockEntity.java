package mods.railcraft.world.level.block.entity;

import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.inventory.PoweredRollingMachineMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredRollingMachineBlockEntity extends RailcraftBlockEntity implements MenuProvider {

  public PoweredRollingMachineBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.POWERED_ROLLING_MACHINE.get(), blockPos, blockState);
  }

  public static void serverTick(Level level, BlockPos blockPos, BlockState blockState,
      PoweredRollingMachineBlockEntity blockEntity) {
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
    return new PoweredRollingMachineMenu(containerId, inventory, this);
  }

  public float getRollingProgress() {
    return 0;
  }
}
