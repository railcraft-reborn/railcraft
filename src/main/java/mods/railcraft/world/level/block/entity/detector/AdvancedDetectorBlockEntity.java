package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.api.container.manipulator.SlotAccessor;
import mods.railcraft.util.container.StackFilter;
import mods.railcraft.world.inventory.detector.AdvancedDetectorMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class AdvancedDetectorBlockEntity extends FilterDetectorBlockEntity {

  public AdvancedDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.ADVANCED_DETECTOR.get(), blockPos, blockState, 9);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    for (var cart : minecarts) {
      if (stream().map(SlotAccessor::item).anyMatch(StackFilter.isCart(cart))) {
        return Redstone.SIGNAL_MAX;
      }
    }
    return Redstone.SIGNAL_NONE;
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new AdvancedDetectorMenu(id, inventory, this);
  }
}
