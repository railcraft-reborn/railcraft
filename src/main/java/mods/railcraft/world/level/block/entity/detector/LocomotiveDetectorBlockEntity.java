package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.entity.vehicle.locomotive.Locomotive;
import mods.railcraft.world.inventory.detector.LocomotiveDetectorMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class LocomotiveDetectorBlockEntity extends FilterDetectorBlockEntity {

  public LocomotiveDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.LOCOMOTIVE_DETECTOR.get(), blockPos, blockState, 2);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    var primary = getItem(0);
    var secondary = getItem(1);
    for (var cart : minecarts) {
      if (cart instanceof Locomotive locomotive) {
        var matches = DyeColor.getColor(primary) == locomotive.getPrimaryDyeColor() &&
            DyeColor.getColor(secondary) == locomotive.getSecondaryDyeColor();
        if (matches) {
          return Redstone.SIGNAL_MAX;
        }
      }
    }
    return Redstone.SIGNAL_NONE;
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new LocomotiveDetectorMenu(id, inventory, this);
  }
}
