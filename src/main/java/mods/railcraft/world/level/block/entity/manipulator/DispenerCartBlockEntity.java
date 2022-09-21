package mods.railcraft.world.level.block.entity.manipulator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class DispenerCartBlockEntity extends ManipulatorBlockEntity implements MenuProvider {

  public DispenerCartBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
    super(type, pos, state);
  }

  @Override
  protected boolean hasWorkForCart(AbstractMinecart cart) {
    return false;
  }

  @Override
  protected void processCart(AbstractMinecart cart) {

  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory,
      Player pPlayer) {
    return null;
  }
}
