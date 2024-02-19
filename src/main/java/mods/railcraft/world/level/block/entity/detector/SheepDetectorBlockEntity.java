package mods.railcraft.world.level.block.entity.detector;

import java.util.List;
import org.jetbrains.annotations.Nullable;
import mods.railcraft.world.inventory.detector.SheepDetectorMenu;
import mods.railcraft.world.level.block.entity.RailcraftBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.redstone.Redstone;

public class SheepDetectorBlockEntity extends FilterDetectorBlockEntity {

  public SheepDetectorBlockEntity(BlockPos blockPos, BlockState blockState) {
    super(RailcraftBlockEntityTypes.SHEEP_DETECTOR.get(), blockPos, blockState, 1);
  }

  @Override
  protected int testCarts(List<AbstractMinecart> minecarts) {
    var dyeItem = this.getItem(0);
    for (var cart : minecarts) {
      var passengers = cart.getPassengers();
      if (passengers.stream()
          .filter(Sheep.class::isInstance)
          .map(Sheep.class::cast)
          .anyMatch(sheep -> !sheep.isBaby() &&
              !sheep.isSheared() &&
              (dyeItem.isEmpty() || sheep.getColor() == DyeColor.getColor(dyeItem)))) {
        return Redstone.SIGNAL_MAX;
      }
    }
    return Redstone.SIGNAL_NONE;
  }

  @Nullable
  @Override
  public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
    return new SheepDetectorMenu(id, inventory, this);
  }
}
